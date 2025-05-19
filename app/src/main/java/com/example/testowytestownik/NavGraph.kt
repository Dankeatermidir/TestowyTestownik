package com.example.testowytestownik

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    fileManager: FileManager,
) {
    NavHost (
        navController = navController,
        startDestination = Screen.Menu.route
    ){
        composable(
            route = Screen.Menu.route
        ){
            MainMenu(navController, fileManager)
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
            route = Screen.Stat.route
        ){
            StatisticsScreen(navController)
        }
        composable(
            route = Screen.Sett.route
        ){
            SettingsScreen(navController)
        }
    }
}
