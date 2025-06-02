package com.example.testowytestownik.ui.screen

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.data.storage.copyFilesToInternalStorage
import com.example.testowytestownik.ui.components.MenuButton
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.DatabaseManager
import com.example.testowytestownik.viewmodel.FileManager
import com.example.testowytestownik.viewmodel.SettingsManager
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    navController : NavController,
    fileManager: FileManager,
    databaseManager: DatabaseManager,
    settingsManager: SettingsManager,
    folderName: String = "./" // optional: browse a subfolder
) {
    val context = LocalContext.current

    val permissionsToRequest = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    val getUserFolder = rememberLauncherForActivityResult(
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
    var selectedFolder by remember { mutableStateOf<File?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf("") }
    var files by remember { mutableStateOf<List<File>>(emptyList()) }

    fun updateFiles() {
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) dir.mkdirs()
        files = dir.listFiles()?.filter { it.isDirectory }?.toList() ?: emptyList()
    }
    updateFiles()

    databaseManager.controlledUpdate(files, settingsManager.uiState.initRepeats)



    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        stringResource(R.string.back_button_desc),
                        modifier = Modifier
                            .clickable {
                                navController.navigate(route = Screen.Menu.route) {
                                    popUpTo(Screen.Menu.route)
                                }
                            }
                            .size(38.dp)
                    )
                    Text(
                        text = stringResource(R.string.open_new),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(38.dp))
                }
                if (files.isEmpty()) {
                    Text(stringResource(R.string.testo_missing))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp)
                    ) {
                        items(files.size) { file ->
                            var itemOffset by remember { mutableStateOf(Offset.Zero) }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .onGloballyPositioned { layoutCoordinates ->
                                        val pos = layoutCoordinates.localToWindow(Offset.Zero)
                                        itemOffset = pos / 3f
                                    }
                                    .combinedClickable(
                                        onClick = {},//navController.navigate(route = Screen.Quiz.route)
                                        onLongClick = {
                                            selectedFolder = files[file]
                                            menuOffset = itemOffset
                                            showMenu = true
                                        }
                                    ),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Text(
                                    text = files[file].name,
                                    modifier = Modifier
                                        .padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                        }
                    }
                }
                ElevatedButton(
                    modifier = Modifier
                        .padding(horizontal= 10.dp, vertical = 5.dp)
                        .fillMaxWidth(1f)
                        .height(75.dp),
                    onClick = {
                        PermissionResultLauncher.launch(permissionsToRequest)
                        getUserFolder.launch(null)
                        updateFiles()
                        databaseManager.viewModelScope.launch { databaseManager.updateDataBases(files,settingsManager.uiState.initRepeats) }
                    }

                ) {
                    Icon(Icons.Default.AddCircle, stringResource(R.string.add))
                    Text(text = stringResource(R.string.add))
                }
            }
            selectedFolder?.let { folder ->
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(menuOffset.x.dp, menuOffset.y.dp)
                ) {
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            fileManager.deleteFolder(folder)
                            databaseManager.deleteQuizByName(folder.name)
                            updateFiles()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Rename") },
                        onClick = {
                            showMenu = false
                            renameText = folder.name
                            showRenameDialog = true
                        }
                    )
                }
            }
            if (showRenameDialog && selectedFolder != null) {
                AlertDialog(
                    onDismissRequest = { showRenameDialog = false },
                    text = {
                        OutlinedTextField(
                            value = renameText,
                            onValueChange = { renameText = it },
                            label = { Text("New name") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            fileManager.renameFolder(selectedFolder!!, renameText)
                            databaseManager.renameQuiz(selectedFolder!!.name, renameText)
                            showRenameDialog = false
                            updateFiles()
                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showRenameDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}



