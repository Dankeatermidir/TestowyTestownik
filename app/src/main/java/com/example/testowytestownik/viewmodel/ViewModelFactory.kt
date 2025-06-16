package com.example.testowytestownik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testowytestownik.data.model.QuizDao
import com.example.testowytestownik.data.storage.SettingsStore

class ManagementModelFactory(private val quizDao: QuizDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ManagementModel::class.java)) {
            return ManagementModel(quizDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class QuizModelFactory(private val quizDao: QuizDao, private val store: SettingsStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizModel::class.java)) {
            return QuizModel(quizDao, store) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
