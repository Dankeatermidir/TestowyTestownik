package com.example.testowytestownik.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.testowytestownik.data.model.QuizDatabase
import com.example.testowytestownik.data.storage.SettingsStore
import com.example.testowytestownik.ui.navigation.SetupNavGraph
import com.example.testowytestownik.ui.theme.TestowyTestownikTheme
import com.example.testowytestownik.viewmodel.ManagementModel
import com.example.testowytestownik.viewmodel.ManagementModelFactory
import com.example.testowytestownik.viewmodel.ManualModel
import com.example.testowytestownik.viewmodel.QuizModel
import com.example.testowytestownik.viewmodel.QuizModelFactory
import com.example.testowytestownik.viewmodel.SettingsModel


class MainActivity : ComponentActivity() {
    //init navController
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val store = SettingsStore(applicationContext)
        //init DB
        val db = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java, "quiz-db"
        ).build()
        val quizDao = db.quizDao()
        //init viewModels
        val managementModelFactory = ManagementModelFactory(quizDao)
        val managementModel =
            ViewModelProvider(this, managementModelFactory)[ManagementModel::class.java]
        val quizModelFactory = QuizModelFactory(quizDao, store)
        val quizModel = ViewModelProvider(this, quizModelFactory)[QuizModel::class.java]
        val settingsModel = SettingsModel(store)
        val manualModel = ManualModel()
        //start actual activity
        setContent {
            TestowyTestownikTheme {
                navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    managementModel = managementModel,
                    settingsModel = settingsModel,
                    quizModel = quizModel,
                    manualModel = manualModel
                )
            }
        }
    }
}
