package com.example.testowytestownik

sealed class Screen(val route: String) {
    object Menu: Screen(route = "Main_Menu")
    object Info: Screen(route = "Info_Screen")
    object Stat: Screen(route = "Statistics_Screen")
    object Sett: Screen(route = "Settings_Screen")
    object Inst: Screen(route = "Manual_Screen")
    object Quiz: Screen(route = "Question_Screen")
}