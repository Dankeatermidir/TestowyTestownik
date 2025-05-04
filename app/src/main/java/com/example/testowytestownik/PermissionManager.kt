package com.example.testowytestownik

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import android.Manifest

class PermissionManager {

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