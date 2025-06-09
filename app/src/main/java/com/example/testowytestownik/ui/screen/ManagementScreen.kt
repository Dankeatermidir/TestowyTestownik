package com.example.testowytestownik.ui.screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.data.storage.dataStore
import com.example.testowytestownik.data.storage.copyFilesToInternalStorage
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.ManagementModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

/*
Management screen is used to manage Quizzes.
Load them into internal storage and insert save info into database,
Rename and Delete Quizzes.
 */

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManagementScreen(
    navController : NavController,
    managementModel: ManagementModel,
    folderName: String = "./" // optional: browse a subfolder
) {
    val context = LocalContext.current
    var selectedFolder by remember { mutableStateOf<File?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var menuOffset by remember { mutableStateOf(Offset.Zero) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf("") }
    var files by remember { mutableStateOf<List<File>>(emptyList()) }

    var wait = remember{ mutableStateOf(false)}

    //Load Initial number of repeats from user preferences
    val initRepeatsFlow = context.dataStore.data
        .map { it[intPreferencesKey("initial_repeats")] ?: 2 }
    val initRepeats by initRepeatsFlow.collectAsState(initial = 2)

    val coroutine = rememberCoroutineScope()

    //Ask for permissions
    val permissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            managementModel.permissionsToRequest.forEach { permission ->
                managementModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )

    // Pick folder with file manager and copy it to app private files
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


    // Scan files in internal storage
    fun updateFiles() {
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) dir.mkdirs()
        files = dir.listFiles()?.filter { it.isDirectory }?.toList() ?: emptyList()
    }
    updateFiles()

    // Synchronize DB with files
    managementModel.controlledUpdate(files,initRepeats)



    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row( // Title
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
                if (files.isEmpty()) { //if no files in internal storage - display info
                    Text(stringResource(R.string.testo_missing))
                }
                else {
                    LazyColumn( //Display Scrollable list of Quiz folders
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
                                        itemOffset = pos / 3f //check card position to display popupMenu on right height
                                    }
                                    .combinedClickable(
                                        onClick = {
                                            coroutine.launch {
                                                managementModel.updateLastQuiz(files[file].name)
                                            }
                                            navController.navigate(route = Screen.Quiz.route){
                                                popUpTo(Screen.Menu.route) {
                                                    inclusive = false
                                                }
                                            }
                                        },//navController.navigate(route = Screen.Quiz.route) //navigate to quiz
                                        onLongClick = { // display popupMenu on long press
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
                ElevatedButton( // Add new folder on click
                    modifier = Modifier
                        .padding(horizontal= 10.dp, vertical = 5.dp)
                        .fillMaxWidth(1f)
                        .height(75.dp),
                    onClick = {
                        permissionResultLauncher.launch(managementModel.permissionsToRequest)
                        getUserFolder.launch(null)
                        updateFiles() //update files list and synchronize DB after adding folder.
                        managementModel.updateDataBases(files,initRepeats)
                    }

                ) {
                    Icon(Icons.Default.AddCircle, stringResource(R.string.add))
                    Text(text = stringResource(R.string.add))
                }
            }
            selectedFolder?.let { folder ->
                DropdownMenu( //popupMenu
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(menuOffset.x.dp, menuOffset.y.dp)
                ) {
                    DropdownMenuItem(// Delete quiz from storage and DB
                        text = { Text("Delete") },
                        onClick = {
                            showMenu = false
                            managementModel.deleteFolder(folder)
                            managementModel.deleteQuizByName(folder.name)
                            updateFiles()
                        }
                    )
                    DropdownMenuItem(// Start rename dialog popup
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
                AlertDialog( // rename dialog popup
                    onDismissRequest = { showRenameDialog = false },
                    text = {
                        OutlinedTextField(
                            value = renameText,
                            onValueChange = { renameText = it },
                            label = { Text("New name") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { //rename folder and entry in DB
                            managementModel.renameFolder(selectedFolder!!, renameText)
                            managementModel.renameQuiz(selectedFolder!!.name, renameText)
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
