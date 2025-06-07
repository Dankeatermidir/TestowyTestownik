package com.example.testowytestownik.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.Question
import com.example.testowytestownik.data.model.QuizDao
import com.example.testowytestownik.data.storage.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


data class QueFile(
    val question: String,
    val typeCorrect: String,
    val answers: List<String>
)

class QuizModel(private val quizDao: QuizDao) : ViewModel(){

    private var questionsTemp:List<Question?>?=listOf(null)

    val lastQuiz: StateFlow<String> = quizDao.getLastQuizStream()
        .map { it ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    init {
        loadLastQuiz()

        viewModelScope.launch {
            lastQuiz.collect { quizName ->
                if (quizName.isNotBlank()) {
                    loadQuestions(quizName)
                }
            }
        }
    }

    private suspend fun loadQuestions(quizName: String)
    {
        questionsTemp=quizDao.getQuestionsForQuiz(quizName)
    }

    private fun loadLastQuiz() {
        viewModelScope.launch {
            val test = quizDao.getLastQuiz()
            if(test == null)
            {
                quizDao.initLastQuiz()
            }
        }
    }


    fun resetQuiz(name: String, initRepeats: Int){
        viewModelScope.launch {
            quizDao.resetQuiz(name,initRepeats)
        }
    }

    fun changeRepeatsLeft(quizName: String, questionName: String, newRepeats: Int) {
        viewModelScope.launch {
            quizDao.changeRepeatsLeft(quizName, questionName, newRepeats)
        }
    }

    fun onCorrectAnswer(quizName: String, questionName: String){
        viewModelScope.launch {
            var newRepeats = quizDao.getRepeatsLeft(quizName,questionName)
            if (newRepeats == null) newRepeats = 0
            newRepeats -= 1
            if (newRepeats <= 0){
                var questionLeft = quizDao.getQuestionLeft(quizName)
                if (questionLeft != null){
                    questionLeft -= 1
                    quizDao.updateQuestionLeft(quizName,questionLeft)
                }
            }
            changeRepeatsLeft(quizName,questionName,newRepeats)
            quizDao.updateCorrectAnswers(quizName,1)

        }
    }

    fun onWrongAnswer(quizName: String, questionName: String, extraRepeats: Int, maxRepeats: Int){
        viewModelScope.launch {
            changeRepeatsLeft(quizName,questionName,extraRepeats)
            var newRepeats = quizDao.getRepeatsLeft(quizName,questionName)
            if (newRepeats == null) newRepeats = 0
            newRepeats = (newRepeats + extraRepeats) % (maxRepeats + 1)
            quizDao.updateWrongAnswers(quizName,newRepeats)
        }
    }


    fun readFile(context: Context, filename: String): List<String> {
        return try {
            File(context.filesDir, filename)
                .bufferedReader()
                .use { reader ->
                    reader.readLines().ifEmpty { listOf("") }
                }
        } catch (e: FileNotFoundException) {
            listOf("")
        } catch (e: IOException) {
            listOf("")//Error: ${e.message}")
        }
    }

    fun getQuestion(context: Context, quizName: String, questionName: String): QueFile
    {
        val listToParse=readFile(context, "${quizName}/${questionName}.txt")
        return QueFile(listToParse[1],listToParse[0],listToParse.subList(2, listToParse.size))
    }

    fun drewQuestion(quizName: String): String
    {
        var que=""
        viewModelScope.launch {
            if (questionsTemp != null)
            {
                que=questionsTemp!!.random()!!.questionName
            }
        }
        return que
    }


    fun correctAnswersList(que:QueFile): List<Int>
    {
        val correct = mutableListOf<Int>()
        val end=que.typeCorrect.length-1
        for(i in 1..end)
        {
            if(que.typeCorrect[i]=='1')
            {
                correct.add(i-1)
            }
        }
        return correct
    }

    fun isPicInQue(quizName: String, questionName: String): Boolean
    {
        return false
    }

    fun isPicInAns(quizName: String, questionName: String): Boolean
    {
        return false
    }

    fun isY(quizName: String, questionName: String): Boolean
    {
        return false
    }


}