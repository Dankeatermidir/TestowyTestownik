package com.example.testowytestownik.data.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Quiz::class, Question::class, LastQuiz::class], version = 1)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}
