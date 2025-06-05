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
import com.example.testowytestownik.ui.screen.StatisticsScreen
import com.example.testowytestownik.viewmodel.InfoModel
import com.example.testowytestownik.viewmodel.ManagementModel
import com.example.testowytestownik.viewmodel.ManualModel
import com.example.testowytestownik.viewmodel.QuizModel
import com.example.testowytestownik.viewmodel.SettingsModel
import com.example.testowytestownik.viewmodel.StatisticsModel

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    managementModel: ManagementModel,
    settingsModel: SettingsModel,
    quizModel: QuizModel,
    infoModel: InfoModel,
    statisticsModel: StatisticsModel,
    manualModel: ManualModel
) {
    NavHost (
        navController = navController,
        startDestination = Screen.Menu.route
    ){
        composable( //navigate to Main Menu
            route = Screen.Menu.route
        ){
            MainMenu(navController)
        }
        composable( //navigate to Information Screen
            route = Screen.Info.route
        ){
            InfoScreen(navController)
        }
        composable( //navigate to Manual Screen
            route = Screen.Inst.route
        ){
            ManualScreen(navController)
        }
        composable( //navigate to Statistics Screen
            route = Screen.Stat.route
        ){
            StatisticsScreen(navController)
        }
        composable( //navigate to Settings Screen
            route = Screen.Sett.route
        ){
            SettingsScreen(navController, settingsModel)
        }
        composable( //navigate to Quiz Management Screen
            route = Screen.Mgmt.route
        ){
            ManagementScreen(navController, managementModel,"./")
        }
    }
}
