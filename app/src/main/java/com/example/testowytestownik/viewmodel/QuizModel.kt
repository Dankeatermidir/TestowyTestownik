package com.example.testowytestownik.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.QuizDao
import com.example.testowytestownik.data.storage.SettingsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


data class QueFile(
    val question: String,
    val typeCorrect: String,
    val answers: List<String>
)

class QuizModel(private val quizDao: QuizDao) : ViewModel(){

    val lastQuiz: StateFlow<String> = quizDao.getLastQuizStream()
        .map { it ?: "" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    init {
        loadLastQuiz()
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

    fun getQuestion(quizName: String, questionName: String): QueFile
    {
        return QueFile("Rozważmy układ równań różniczkowych du/dt. = Au. Ile wynoszą wartości własne macierzy A, gdy A= [1 2 ; 1 2]","X1010",listOf("a. 0 oraz (-3)", "b. 1 oraz 2", "c. 0 oraz (3)", "d. 2 oraz 2"))
    }

    fun drewQuestion(quizName: String): String
    {
        var que=""
        viewModelScope.launch {
            val answers=quizDao.getQuestionsForQuiz(quizName)
            var ansRepeats:Int?=0
            while(ansRepeats==0)
            {
                que=answers.random().questionName
                ansRepeats=quizDao.getRepeatsLeft(quizName,que)
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