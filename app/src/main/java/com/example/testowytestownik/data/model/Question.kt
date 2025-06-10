package com.example.testowytestownik.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["quizName"],
        childColumns = ["parentQuiz"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey val questionName: String,
    val parentQuiz: String,
    val repeatsLeft: Int
)