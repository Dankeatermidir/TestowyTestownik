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
import com.example.testowytestownik.data.storage.SettingsStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
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


    private var lastQuizReady by mutableStateOf(false)
    private var loadQuizReady by mutableStateOf(false)
    var isReady by mutableStateOf(false)
//        private set

    private var questionsTemp:List<Question?>?=listOf(null)

    val lastQuiz: StateFlow<String> = quizDao.getLastQuizStream()
        .map { it ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val remainingQuestions: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.remainingQuestions(quizName)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val doneQuestions: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.doneQuestions(quizName)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val allQuestions: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.allQuestions(quizName)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val wrongAnswers: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.wrongAnswers(quizName)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val correctAnswers: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.correctAnswers(quizName)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
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
        if(lastQuizReady)
        {
            this.questionsTemp=quizDao.getQuestionsForQuiz(quizName)
        }
        else
        {
            loadQuestions(quizName)
        }
        isReady=true
    }

    private fun loadLastQuiz() {
        viewModelScope.launch {
            val test = quizDao.getLastQuiz()
            if(test == null)
            {
                quizDao.initLastQuiz()
            }
        }
        lastQuizReady=true
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
            if (questionsTemp != null && isReady)
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

    fun getImage(context:Context, name:String):File
    {
        return File(context.filesDir, name)
    }

    fun parseImgageName(name: String): String
    {
        val input = name
        val regex = "\\[img](.+?)\\[/img]".toRegex()
        val matchResult = regex.find(input)
        return matchResult?.groupValues?.get(1) ?: ""
    }

    fun isY(quizName: String, questionName: String): Boolean
    {
        return false
    }



    //timer

    private var startTime = System.currentTimeMillis()

    var shouldResetTimer = false

    fun resetTimer() {
        startTime = System.currentTimeMillis()
    }

    fun getElapsedTime(): List<Int> {
        var temp = (System.currentTimeMillis() - startTime) / 1000
        val hours = (temp / 60 / 60).toInt()
        temp -= (hours * 60 * 60)
        val minutes = (temp / 60).toInt()
        temp -= (minutes * 60)
        return listOf(hours, minutes, temp.toInt()) // to seconds
    }

}