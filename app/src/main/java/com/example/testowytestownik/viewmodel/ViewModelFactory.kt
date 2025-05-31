package com.example.testowytestownik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Database
import com.example.testowytestownik.data.storage.QuizDao

class QuizViewModelFactory(private val quizDao: QuizDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatabaseManager::class.java)) {
            return DatabaseManager(quizDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}