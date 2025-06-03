package com.example.testowytestownik.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.Question
import com.example.testowytestownik.data.model.Quiz
import com.example.testowytestownik.data.model.QuizDao
import kotlinx.coroutines.launch
import java.io.File

class ManagementModel(private val quizDao: QuizDao) : ViewModel(){
    val PermissionDialogQueue = mutableStateListOf<String>()

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

    fun renameFolder(folder: File, newName: String) {
        viewModelScope.launch {
            val newFile = File(folder.parentFile, newName)
            if (!newFile.exists()) {
                folder.renameTo(newFile)
            }
        }
    }

    private var wasUpdated = false
    fun controlledUpdate(files: List<File>, defaultRepeats: Int) {
        viewModelScope.launch {
            if (!wasUpdated) {
                updateDataBases(files, defaultRepeats)
                wasUpdated = true
            }
        }
    }

    fun renameQuiz(oldName: String, newName: String){
        viewModelScope.launch {
            quizDao.renameQuizAndQuestions(oldName,newName)
        }
    }

    fun insertQuizWithQuestions(quiz: Quiz, questions: List<Question>) {
        viewModelScope.launch {
            quizDao.insertQuizWithQuestions(quiz, questions)
        }
    }

    fun deleteQuizByName(name: String){
        viewModelScope.launch {
            quizDao.deleteQuizByName(name)
        }
    }

    fun updateDataBases(files: List<File>, defaultRepeats: Int){
        viewModelScope.launch {
            val names = quizDao.getAllQuizNames().toMutableList()
            for (dir in files) {
                if (!dir.isDirectory) continue
                if (dir.name in names){
                    names.remove(dir.name)
                    continue
                }

                val quizName = dir.name
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

                insertQuizWithQuestions(quiz, questions)
            }
            if (names.size != 0) {
                for (name in names){
                    quizDao.deleteQuizByName(name)
                }
            }
        }
    }
}