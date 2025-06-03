package com.example.testowytestownik.data.model

import androidx.room.Embedded
import androidx.room.Relation


data class Quizzes(
    @Embedded val quiz: Quiz,

    @Relation(
        parentColumn = "quizName",
        entityColumn = "parentQuiz"
    )
    val questions: List<Question>
)