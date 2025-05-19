package com.example.testowytestownik

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

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




}