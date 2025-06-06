package com.example.testowytestownik.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


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

    @Query("DELETE FROM Quiz WHERE quizName = :quizName")
    suspend fun deleteQuizByName(quizName: String)

    @Query("UPDATE Quiz SET quizUri = :newUri WHERE quizName = :quizName")
    suspend fun updateUri(quizName: String, newUri: String)

    @Query("SELECT questionLeft FROM Quiz WHERE quizName = :quizName")
    suspend fun getQuestionLeft(quizName: String) : Int?

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

    @Query("SELECT quizName FROM Quiz")
    suspend fun getAllQuizNames(): List<String>

    @Query("UPDATE Quiz SET quizName = :newName WHERE quizName = :oldName")
    suspend fun renameQuiz(oldName: String, newName: String)

    @Query("UPDATE Question SET parentQuiz = :newName WHERE parentQuiz = :oldName")
    suspend fun updateQuestionParentQuiz(oldName: String, newName: String)

    @Transaction
    suspend fun renameQuizAndQuestions(oldName: String, newName: String) {
        renameQuiz(oldName, newName)
        updateQuestionParentQuiz(oldName, newName)
    }

    @Query("SELECT quizName FROM Quiz")
    fun observeAllQuizNames(): LiveData<List<String>>

    @Query("""
    UPDATE Question 
    SET repeatsLeft = :newRepeats 
    WHERE questionName = :questionName AND parentQuiz = :quizName
    """)
    suspend fun changeRepeatsLeft(quizName: String, questionName: String, newRepeats: Int)

    @Query("""
    SELECT repeatsLeft 
    FROM Question 
    WHERE questionName = :questionName AND parentQuiz = :quizName
    """)
    suspend fun getRepeatsLeft(quizName: String, questionName: String): Int?

    @Query("UPDATE LastQuiz SET quizName=:quizName WHERE num=1")
    suspend fun setLastQuiz(quizName: String)

    @Query("SELECT quizName FROM LastQuiz WHERE num=1")
    suspend fun getLastQuiz():String?

    @Query("SELECT quizName FROM LastQuiz WHERE num=1")
    fun getLastQuizStream(): Flow<String?>

    @Query("INSERT INTO LastQuiz (num, quizName) VALUES (1, '')")
    suspend fun initLastQuiz()

}