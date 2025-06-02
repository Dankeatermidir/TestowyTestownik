package com.example.testowytestownik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testowytestownik.data.storage.Question
import com.example.testowytestownik.data.storage.Quiz
import com.example.testowytestownik.data.storage.QuizDao
import com.example.testowytestownik.data.storage.Quizzes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DatabaseManager(private val quizDao: QuizDao) : ViewModel() {

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

    fun deleteQuiz(quiz: Quiz) {
        viewModelScope.launch {
            quizDao.deleteQuiz(quiz)
        }
    }

    fun deleteQuizByName(name: String){
        viewModelScope.launch {
            quizDao.deleteQuizByName(name)
        }
    }

    fun updateQuestionLeft(quizName: String, newCount: Int) {
        viewModelScope.launch {
            quizDao.updateQuestionLeft(quizName, newCount)
        }
    }

    fun updateWrongAnswers(quizName: String, wrong: Int) {
        viewModelScope.launch {
            quizDao.updateWrongAnswers(quizName, wrong)
        }
    }

    fun updateCorrectAnswers(quizName: String, correct: Int) {
        viewModelScope.launch {
            quizDao.updateCorrectAnswers(quizName, correct)
        }
    }

    fun updateRepeatsLeft(questionName: String, repeats: Int) {
        viewModelScope.launch {
            quizDao.updateQuestionRepeatsLeft(questionName, repeats)
        }
    }

    fun resetQuiz(quizName: String, defaultRepeats: Int) {
        viewModelScope.launch {
            quizDao.resetQuiz(quizName, defaultRepeats)
        }
    }

    suspend fun getQuizWithQuestions(quizName: String): Quizzes? {
        return quizDao.getQuizWithQuestions(quizName)
    }




    suspend fun updateDataBases(files: List<File>, defaultRepeats: Int){
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
    val allQuizNames: LiveData<List<String>> = quizDao.observeAllQuizNames()

}