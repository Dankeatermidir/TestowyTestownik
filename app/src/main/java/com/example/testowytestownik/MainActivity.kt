package com.example.testowytestownik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.testowytestownik.ui.theme.TestowyTestownikTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    val fileManager = FileManager()
    lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestowyTestownikTheme {
                navController = rememberNavController()
                SetupNavGraph(navController = navController, fileManager)
            }
        }
    }
}
