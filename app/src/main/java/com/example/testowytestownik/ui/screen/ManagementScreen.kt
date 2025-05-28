package com.example.testowytestownik.ui.screen

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.testowytestownik.data.storage.copyFilesToInternalStorage
import com.example.testowytestownik.ui.components.MenuButton
import com.example.testowytestownik.viewmodel.FileManager
import java.io.File

@Composable
fun ManagementScreen(
    navController : NavController,
    fileManager: FileManager,
    folderName: String = "MyAppData" // optional: browse a subfolder
) {
    val context = LocalContext.current

    val permissionsToRequest = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val GetUserFolder = rememberLauncherForActivityResult(
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
    val files = remember {
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) dir.mkdirs()
        dir.listFiles()?.toList() ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Contents of: /data/data/${context.packageName}/files/$folderName",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (files.isEmpty()) {
            Text("No files found in $folderName.")
        } else {
            LazyColumn {
                items(
                    count = files.size,
                    key = { index -> files[index] }, // optional
                    itemContent = { index ->
                        Text(text = files[index].name)
                    }
                )
            }
        }
        MenuButton(
            "DODAJ",
            Icons.Default.Add
        ) {
            PermissionResultLauncher.launch(permissionsToRequest)
            GetUserFolder.launch(null)
        }
    }

}
