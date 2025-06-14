package com.example.testowytestownik.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.testowytestownik.ui.screen.ManagementScreen
import com.example.testowytestownik.ui.screen.InfoScreen
import com.example.testowytestownik.ui.screen.MainMenu
import com.example.testowytestownik.ui.screen.ManualScreen
import com.example.testowytestownik.ui.screen.SettingsScreen
import com.example.testowytestownik.ui.screen.QuizScreen
import com.example.testowytestownik.viewmodel.ManagementModel
import com.example.testowytestownik.viewmodel.ManualModel
import com.example.testowytestownik.viewmodel.QuizModel
import com.example.testowytestownik.viewmodel.SettingsModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    managementModel: ManagementModel,
    settingsModel: SettingsModel,
    quizModel: QuizModel,
    manualModel: ManualModel
) {
    NavHost (
        navController = navController,
        startDestination = Screen.Menu.route
    ){
        composable(
            route = Screen.Menu.route
        ){
            MainMenu(navController)
        }
        composable(
            route = Screen.Info.route
        ){
            InfoScreen(navController)
        }
        composable(
            route = Screen.Inst.route
        ){
            ManualScreen(navController)
        }
        composable(
            route = Screen.Sett.route
        ){
            SettingsScreen(navController, settingsModel)
        }
        composable(
            route = Screen.Mgmt.route
        ){
            ManagementScreen(navController, managementModel)
        }
        composable(
            route = Screen.Quiz.route
        ){
            QuizScreen(navController, quizModel)
        }
    }
}
