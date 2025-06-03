package com.example.testowytestownik.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.QuizDao
import kotlinx.coroutines.launch

class QuizModel(private val quizDao: QuizDao) : ViewModel(){

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



}