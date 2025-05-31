package com.example.testowytestownik.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.storage.Question
import com.example.testowytestownik.data.storage.Quiz
import com.example.testowytestownik.data.storage.QuizDao
import com.example.testowytestownik.data.storage.Quizzes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatabaseManager(private val quizDao: QuizDao) : ViewModel() {

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

    //val allQuizzes: LiveData<List<Quiz>> = quizDao.getAllQuizzes().asLiveData()
}