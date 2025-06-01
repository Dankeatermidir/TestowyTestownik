package com.example.testowytestownik.data.storage

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction

@Entity
data class Quiz(
    @PrimaryKey val quizName: String,
    val questionNum: Int,
    val questionLeft: Int,
    val wrongAnswers: Int,
    val correctAnswers: Int,
    val quizUri: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["quizName"],
        childColumns = ["parentQuiz"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Question(
    @PrimaryKey val questionName: String,
    val parentQuiz: String,
    val repeatsLeft: Int
)

data class Quizzes(
    @Embedded val quiz: Quiz,

    @Relation(
        parentColumn = "quizName",
        entityColumn = "parentQuiz"
    )
    val questions: List<Question>
)

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Transaction
    suspend fun insertQuizWithQuestions(quiz: Quiz, questions: List<Question>){
        insertQuiz(quiz)
        insertQuestions(questions)
    }

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("UPDATE Quiz SET quizUri = :newUri WHERE quizName = :quizName")
    suspend fun updateUri(quizName: String, newUri: String)

    @Query("UPDATE Quiz SET questionLeft = :newCount WHERE quizName = :quizName")
    suspend fun updateQuestionLeft(quizName: String, newCount: Int)

    @Query("UPDATE Quiz SET wrongAnswers = :wrong WHERE quizName = :quizName")
    suspend fun updateWrongAnswers(quizName: String, wrong: Int)

    @Query("UPDATE Quiz SET correctAnswers = :correct WHERE quizName = :quizName")
    suspend fun updateCorrectAnswers(quizName: String, correct: Int)

    @Query("UPDATE Question SET repeatsLeft = :repeats WHERE questionName = :questionName")
    suspend fun updateQuestionRepeatsLeft(questionName: String, repeats: Int)

    @Transaction
    @Query("SELECT * FROM Quiz WHERE quizName = :quizName")
    suspend fun getQuizWithQuestions(quizName: String): Quizzes?

    @Query("SELECT * FROM Quiz")
    suspend fun getAllQuizzes(): List<Quiz>

    @Query("SELECT * FROM Question WHERE parentQuiz = :quizName")
    suspend fun getQuestionsForQuiz(quizName: String): List<Question>

    @Query("DELETE FROM Quiz")
    suspend fun deleteAllQuizzes()

    @Query("UPDATE Question SET repeatsLeft = :defaultRepeats WHERE parentQuiz = :quizName")
    suspend fun resetQuestionRepeats(quizName: String, defaultRepeats: Int)

    @Query("""
        UPDATE Quiz 
        SET 
            wrongAnswers = 0, 
            correctAnswers = 0, 
            questionLeft = questionNum 
        WHERE quizName = :quizName
    """)
    suspend fun resetQuizStats(quizName: String)

    @Transaction
    suspend fun resetQuiz(quizName: String, defaultRepeats: Int) {
        resetQuestionRepeats(quizName, defaultRepeats)
        resetQuizStats(quizName)
    }
}