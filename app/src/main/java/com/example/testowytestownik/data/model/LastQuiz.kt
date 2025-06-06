package com.example.testowytestownik.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LastQuiz")
data class LastQuiz(
    @PrimaryKey val num: Int = 1,
    val quizName: String = ""
)