package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.filled.Create
import androidx.compose.ui.platform.LocalContext

import com.example.testowytestownik.viewmodel.FileManager
import com.example.testowytestownik.ui.components.MenuButton
import com.example.testowytestownik.R
import com.example.testowytestownik.data.storage.copyFilesToInternalStorage
import com.example.testowytestownik.ui.navigation.Screen

@Composable
fun MainMenu(
    navController: NavController,
) {
    val context = LocalContext.current



    Surface{
        Column (
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 30.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = stringResource(R.string.main_menu),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            MenuButton(
                stringResource(R.string.settings),
                Icons.Default.Settings
            ) { navController.navigate(route = Screen.Sett.route) }
            MenuButton(
                stringResource(R.string.stats),
                Icons.AutoMirrored.Filled.List
            ) { navController.navigate(route = Screen.Stat.route) }
            MenuButton(
                stringResource(R.string.open_last),
                Icons.Default.PlayArrow
            ) {
                //navController.navigate(route = Screen.Quiz.route)
            }
            MenuButton(
                stringResource(R.string.open_new),
                Icons.Filled.Create
            ) {
                navController.navigate(route = Screen.Mgmt.route)
            }
            MenuButton(
                stringResource(R.string.intruct),
                Icons.Default.MoreVert
            ) { navController.navigate(route = Screen.Inst.route) }
            MenuButton(
                stringResource(R.string.info),
                Icons.Default.Info,
            ) {
                navController.navigate(route = Screen.Info.route) }
        }
    }
}







/*
@Preview
@Composable
fun PreviewMenu() {
    MainMenu()
}
*/
