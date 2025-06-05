package com.example.testowytestownik.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.quizStore by preferencesDataStore(name="quiz")

class QuizStore(private val context: Context) {
    private val LAST_QUIZ = stringPreferencesKey("last_quiz")

    val defaultQuiz = QuizState()

    fun loadSettings(): Flow<QuizState> = context.dataStore.data
        .map { prefs ->
            QuizState(
                lastQuiz = prefs[LAST_QUIZ] ?: defaultQuiz.lastQuiz
            )
        }
    suspend fun saveLastQuiz(name: String)
    {
        context.quizStore.edit { it[LAST_QUIZ] = name }
    }


}