package com.example.testowytestownik.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.BzztMachen
import com.example.testowytestownik.data.model.Question
import com.example.testowytestownik.data.model.QuizDao
import com.example.testowytestownik.data.storage.SettingsState
import com.example.testowytestownik.data.storage.SettingsStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import okhttp3.Address
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


data class QueFile(
    val question: String,
    val typeCorrect: String,
    val answers: List<String>
)

data class YQueFile(
    val question: String,
    val correct: List<Int>,
    val answers: List<List<String>>
)

class QuizModel(private val quizDao: QuizDao, private val store: SettingsStore) : ViewModel() {

    // settings state
    var state by mutableStateOf(SettingsState())

    init { // load Preferences on init
        viewModelScope.launch {
            store.loadSettings().collect { loadedSettings ->
                state = loadedSettings
            }
        }
    }

    private var lastQuizReady by mutableStateOf(false)
    var isReady by mutableStateOf(false)

    var currentRepeats = 0
    var questionsTemp: List<Question?>? = listOf(null)

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
                .filterNotNull()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val doneQuestions: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.doneQuestions(quizName)
                .filterNotNull()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val allQuestions: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.allQuestions(quizName)
                .filterNotNull()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val wrongAnswers: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.wrongAnswers(quizName)
                .filterNotNull()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )


    @OptIn(ExperimentalCoroutinesApi::class)
    val correctAnswers: StateFlow<Int> =
        lastQuiz.flatMapLatest { quizName ->
            quizDao.correctAnswers(quizName)
                .filterNotNull()
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


    private suspend fun loadQuestions(quizName: String) {
        if (lastQuizReady) {
            this.questionsTemp = quizDao.getQuestionsForQuiz(quizName)
        } else {
            loadQuestions(quizName)
        }
        isReady = true
    }

    private fun loadLastQuiz() {
        viewModelScope.launch {
            val test = quizDao.getLastQuiz()
            if (test == null) {
                quizDao.initLastQuiz()
            }
        }
        lastQuizReady = true
    }


    fun resetQuiz(name: String, initRepeats: Int = state.initRepeats) {
        viewModelScope.launch {
            quizDao.resetQuiz(name, initRepeats)
            loadQuestions(name)
        }
    }

    fun onCorrectAnswer(quizName: String, questionName: String) {
        viewModelScope.launch {
            var newRepeats = quizDao.getRepeatsLeft(quizName, questionName)
            if (newRepeats == null) newRepeats = 0
            newRepeats -= 1
            if (newRepeats <= 0) {
                var questionLeft = quizDao.getQuestionLeft(quizName)
                if (questionLeft != null) {
                    questionLeft -= 1
                    quizDao.updateQuestionLeft(quizName, questionLeft)
                }
                quizDao.updateQuestionRepeatsLeft(questionName, 0)
            } else {
                quizDao.updateQuestionRepeatsLeft(questionName, newRepeats)
            }
            quizDao.updateCorrectAnswers(quizName, quizDao.getIntCorrectAnswers(quizName) + 1)
        }
    }

    fun onWrongAnswer(
        quizName: String,
        questionName: String,
        extraRepeats: Int = state.extraRepeats,
        maxRepeats: Int = state.maxRepeats,
        isBzzt: Boolean = state.hardcoreMode,
        address: String = state.bzztmachenAddress,
        bzztPlayer: Int = state.bzztmachenPlayer
    ) {
        viewModelScope.launch {
            var newRepeats = quizDao.getRepeatsLeft(quizName, questionName)
            if (newRepeats == null) newRepeats = 0
            newRepeats = (newRepeats + extraRepeats) % (maxRepeats + 1)
            quizDao.updateQuestionRepeatsLeft(questionName, newRepeats)
            quizDao.updateWrongAnswers(quizName, quizDao.getIntWrongAnswers(quizName) + 1)
            if (isBzzt){
                val percent = wrongAnswers.value * 100 / (correctAnswers.value + wrongAnswers.value + 1)
                val freq = BzztMachen.lvlFromPercent(percent)
                val url = "http://$address/machen"
                BzztMachen.machen(url = url, player = bzztPlayer, lvl = freq)
            }
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
            listOf("")
        }
    }

    fun getQuestion(context: Context, quizName: String, questionName: String): QueFile {
        val listToParse = readFile(context, "testowniki/${quizName}/${questionName}.txt")
        return QueFile(listToParse[1], listToParse[0], listToParse.subList(2, listToParse.size))
    }


    fun getYQuestion(context: Context, quizName: String, questionName: String): YQueFile {
        val listToParse = readFile(context, "testowniki/${quizName}/${questionName}.txt")
        var ansList = listToParse[0].drop(2).map { it.digitToInt() - 1 }
        val result: List<List<String>> =
            listToParse.subList(2, listToParse.size).map { it.split(";;") }

        return YQueFile(listToParse[1], ansList, result)
    }


    fun drewQuestion(quizName: String): String {
        var que = ""
        viewModelScope.launch {
            if (questionsTemp != null && isReady) {
                val questionTemp = questionsTemp!!.random()!!
                que = questionTemp.questionName
                currentRepeats = quizDao.getRepeatsLeft(questionTemp.parentQuiz, que)!!
            }
            questionsTemp = quizDao.getQuestionsForQuiz(quizName)
        }
        return que
    }


    fun correctAnswersList(que: QueFile): List<Int> {
        val correct = mutableListOf<Int>()
        val end = que.typeCorrect.length - 1
        for (i in 1..end) {
            if (que.typeCorrect[i] == '1') {
                correct.add(i - 1)
            }
        }
        return correct
    }

    fun parseFile(name: String, medium: String): String {
        val input = name
        val regex = "\\[$medium](.+?)\\[/$medium]".toRegex()
        val matchResult = regex.find(input)
        return matchResult?.groupValues?.get(1) ?: ""
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
        return listOf(hours, minutes, temp.toInt())
    }

}