package com.example.testowytestownik.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quiz(
    @PrimaryKey val quizName: String,
    val questionNum: Int,
    val questionLeft: Int,
    val wrongAnswers: Int,
    val correctAnswers: Int,
    val quizUri: String
)

