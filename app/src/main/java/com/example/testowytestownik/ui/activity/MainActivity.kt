package com.example.testowytestownik.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.testowytestownik.ui.theme.TestowyTestownikTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.testowytestownik.data.model.SettingsStore
import com.example.testowytestownik.data.model.dataStore
import com.example.testowytestownik.ui.navigation.SetupNavGraph
import com.example.testowytestownik.viewmodel.FileManager
import com.example.testowytestownik.viewmodel.SettingsManager
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.testowytestownik.data.storage.QuizDao
import com.example.testowytestownik.data.storage.QuizDatabase
import com.example.testowytestownik.viewmodel.DatabaseManager
import com.example.testowytestownik.viewmodel.QuizViewModelFactory


class MainActivity : ComponentActivity() {


    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fileManager = FileManager()
        val store = SettingsStore(applicationContext)
        val settingsManager = SettingsManager(store)

        val db = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java, "quiz-db"
        ).build()

        val quizDao = db.quizDao()
        val quizViewModelFactory = QuizViewModelFactory(quizDao)
        val databaseManager = ViewModelProvider(this, quizViewModelFactory)[DatabaseManager::class.java]

        setContent {
            TestowyTestownikTheme(
                settingsManager
            ) {
                navController = rememberNavController()
                SetupNavGraph(navController = navController, fileManager = fileManager, databaseManager = databaseManager, settingsManager = settingsManager)
            }
        }
    }
}
