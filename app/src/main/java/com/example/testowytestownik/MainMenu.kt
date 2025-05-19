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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

@Composable
fun MainMenu(
    navController: NavController,
    fileManager: FileManager,
) {
    val context = LocalContext.current

    val permissionsToRequest = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            copyFilesToInternalStorage(context, it)
        }
    }
    val PermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                fileManager.onPermissionResult(
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
            ) {
                navController.navigate(route = Screen.Quiz.route)
            }
            MenuButton(
                stringResource(R.string.open_new),
                Icons.Filled.Add
            ) {
                PermissionResultLauncher.launch(permissionsToRequest)
                launcher.launch(null)
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


fun copyFilesToInternalStorage(context: Context, uri: Uri) {
    val contentResolver = context.contentResolver
    val pickedFolder = DocumentFile.fromTreeUri(context, uri)
    val folderName = Random.nextInt(0,999999999).toString()
    val targetDir = File(context.filesDir, folderName).apply { mkdirs() }

    pickedFolder?.listFiles()?.forEach { file ->
        if (file.isFile) {
            val inputStream = contentResolver.openInputStream(file.uri)
            val outFile = File(targetDir, file.name ?: "unknown_file")
            val outputStream = FileOutputStream(outFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
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
