package com.example.testowytestownik.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.BzztMachen
import com.example.testowytestownik.data.storage.FontSize
import com.example.testowytestownik.data.storage.SettingsState
import com.example.testowytestownik.data.storage.SettingsStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class SettingsModel(private val store: SettingsStore) : ViewModel() {

    // settings state to be operated on
    var uiState by mutableStateOf(SettingsState())

    init { // load Preferences on init
        viewModelScope.launch {
            store.loadSettings().collect { loadedSettings ->
                uiState = loadedSettings
            }
        }
    }

    //Just edit Preference functions
    fun toogleDarkMode(enabled: Boolean) {
        uiState = uiState.copy(darkMode = enabled)
        viewModelScope.launch {
            store.saveDarkMode(enabled)
        }
    }

    fun toogleAutoSave(enabled: Boolean) {
        uiState = uiState.copy(autoSave = enabled)
        viewModelScope.launch {
            store.saveAutoSave(enabled)
        }
    }

    fun toogleHardcoreMode(enabled: Boolean) {
        uiState = uiState.copy(hardcoreMode = enabled)
        viewModelScope.launch {
            store.saveHardcoreMode(enabled)
        }
    }

    fun updateFontSize(size: FontSize) {
        uiState = uiState.copy(fontSize = size)
        viewModelScope.launch {
            store.saveFontSize(size)
        }
    }

    fun updateInitRepeats(repeats: Int) {
        uiState = uiState.copy(initRepeats = repeats)
        viewModelScope.launch {
            store.saveInitRepeats(repeats)
        }
    }

    fun updateExtraRepeats(repeats: Int) {
        uiState = uiState.copy(extraRepeats = repeats)
        viewModelScope.launch {
            store.saveExtraRepeats(repeats)
        }
    }

    fun updateMaxRepeats(repeats: Int) {
        uiState = uiState.copy(maxRepeats = repeats)
        viewModelScope.launch {
            store.saveMaxRepeats(repeats)
        }
    }

    var response :String? = null

    fun quickTest(address: String) {
        viewModelScope.launch {
            //val url = "http://$address:80"
            val url = address
            response = BzztMachen.machen(url = url).toString()
        }
    }
}