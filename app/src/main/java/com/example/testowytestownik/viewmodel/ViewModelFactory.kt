package com.example.testowytestownik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testowytestownik.data.model.QuizDao

class ManagementModelFactory(private val quizDao: QuizDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagementModel::class.java)) {
            return  ManagementModel(quizDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class QuizModelFactory(private val quizDao: QuizDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizModel::class.java)) {
            return QuizModel(quizDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class StatisticsModelFactory(private val quizDao: QuizDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsModel::class.java)) {
            return StatisticsModel(quizDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}