package com.example.testowytestownik.viewmodel

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testowytestownik.data.model.Question
import com.example.testowytestownik.data.model.Quiz
import com.example.testowytestownik.data.model.QuizDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

class ManagementModel(private val quizDao: QuizDao) : ViewModel(){

    /*
    Managing permissions, request storage permissions and store result.
    Request only older API permissions, because newer API doesn't need them for picker event.
     */

    val PermissionDialogQueue = mutableStateListOf<String>()

    fun updateLastQuiz(quizName: String)
    {
        viewModelScope.launch {
            quizDao.setLastQuiz(quizName)
        }
    }

    val permissionsToRequest = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !PermissionDialogQueue.contains(permission)) {
            PermissionDialogQueue.add(permission)
        }
    }


    fun deleteFolder(folder: File) {
        viewModelScope.launch {
            folder.deleteRecursively()
        }
    }


    // make sure DB is synced only once one start, unless called directly
    private var wasUpdated = false
    fun controlledUpdate(files: List<File>, defaultRepeats: Int) {
        viewModelScope.launch {
            if (!wasUpdated) {
                updateDataBases(files, defaultRepeats)
                wasUpdated = true
            }
        }
    }

    fun renameQuiz(folder: File, newName: String){
        val oldName = folder.name
        viewModelScope.launch {
            val newFile = File(folder.parentFile, newName)
            if (!newFile.exists()) {
                folder.renameTo(newFile)
            }
            quizDao.renameQuiz(oldName,newName)
        }
    }

    fun insertQuizWithQuestions(quiz: Quiz, questions: List<Question>) {
        viewModelScope.launch {
            quizDao.insertQuizWithQuestions(quiz, questions)
            if(quizDao.getLastQuiz()==null)
            {
                quizDao.initLastQuiz()
            }
        }
    }

    fun deleteQuizByName(name: String){
        viewModelScope.launch {
            quizDao.deleteQuizByName(name)
            if(quizDao.getLastQuiz()?.let { quizDao.getQuestionsForQuiz(it)}?.isEmpty() == true)
            {
                quizDao.setLastQuiz("")
            }
        }
    }

    //copy files to /testowniki
    fun copyFilesToInternalStorage(context: Context, uri: Uri){
        viewModelScope.launch {
            val contentResolver = context.contentResolver
            val pickedFolder = DocumentFile.fromTreeUri(context, uri)
            var folderName = Random.nextInt(0,999999999).toString()
            if (pickedFolder.name != null) { //if folder name is null, name is random numbers
                folderName = pickedFolder.name
            }

            val parentDir = File(context.filesDir, "testowniki").apply { mkdirs() }
            val targetDir = File(parentDir, folderName).apply { mkdirs() }
            pickedFolder?.listFiles()?.forEach { file ->
                if (file.isFile) {
                    val inputStream = contentResolver.openInputStream(file.uri)
                    val outFile = File(targetDir, file.name ?: "unknown_file")
                    val outputStream = FileOutputStream(outFile)

                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }

    // Synchronize DB with files in internal storage
    fun updateDataBases(files: List<File>, defaultRepeats: Int){
        viewModelScope.launch {
            val names = quizDao.getAllQuizNames().toMutableList()
            for (dir in files) {
                if (!dir.isDirectory) continue
                if (dir.name in names){ //skip if Quiz is already in DB
                    names.remove(dir.name)
                    continue
                }

                val quizName = dir.name
                //get name of each file and ensure it's in right extension
                val txtFiles = dir.listFiles() {file -> file.extension.lowercase() == "txt"} ?: continue

                val questions = txtFiles.map{file ->
                    Question(
                        questionName = file.nameWithoutExtension,
                        parentQuiz = quizName,
                        repeatsLeft = defaultRepeats
                    )
                }

                val quiz = Quiz(
                    quizName = quizName,
                    questionNum = questions.size,
                    questionLeft = questions.size,
                    correctAnswers = 0,
                    wrongAnswers = 0,
                    quizUri = quizName
                )
                if (questions.size > 3) insertQuizWithQuestions(quiz, questions)
                else dir.deleteRecursively()
            }
            if (names.size != 0) {
                for (name in names){ //if folder is no longer in internal storage - delete entry
                    quizDao.deleteQuizByName(name)
                }
            }
        }
    }
}