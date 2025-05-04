package com.example.testowytestownik

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import androidx.navigation.compose.rememberNavController

@Composable
fun MainMenu(
    navController: NavController,
    permissionManager: PermissionManager
) {

    val permissionsToRequest = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    val PermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                permissionManager.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )


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
            ) {PermissionResultLauncher.launch(permissionsToRequest)}
            MenuButton(
                stringResource(R.string.open_new),
                Icons.Filled.Add
            ) {PermissionResultLauncher.launch(permissionsToRequest)}
            MenuButton(
                stringResource(R.string.intruct),
                Icons.Default.MoreVert
            ) { navController.navigate(route = Screen.Inst.route) }
            MenuButton(stringResource(R.string.info), Icons.Default.Info,) {
                navController.navigate(
                    route = Screen.Info.route
                )
            }
        }
    }
}

@Composable
fun MenuButton(name:String, icon: ImageVector, onclick: () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(1f)
            .height(75.dp),
        onClick = onclick

    ) {
        Icon(icon, name)
        Text(text = name)
    }
}

/*
@Preview
@Composable
fun PreviewMenu() {
    MainMenu()
}
*/
