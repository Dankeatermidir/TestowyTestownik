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


class MainActivity : ComponentActivity() {


    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fileManager = FileManager()
        val store = SettingsStore(applicationContext)
        val settingsManager = SettingsManager(store)
        setContent {
            TestowyTestownikTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController, fileManager, settingsManager)
            }
        }
    }
}
