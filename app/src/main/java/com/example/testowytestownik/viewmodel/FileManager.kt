package com.example.testowytestownik.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class FileManager: ViewModel() {


    val PermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        PermissionDialogQueue.removeAt(0)
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !PermissionDialogQueue.contains(permission)) {
            PermissionDialogQueue.add(permission)
        }
    }

    fun deleteFolder(folder: File) {
        viewModelScope.launch {
            folder.deleteRecursively()
        }
    }

    fun renameFolder(folder: File, newName: String) {
        viewModelScope.launch {
            val newFile = File(folder.parentFile, newName)
            if (!newFile.exists()) {
                folder.renameTo(newFile)
            }
        }
    }

}