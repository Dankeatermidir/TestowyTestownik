package com.example.testowytestownik.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testowytestownik.data.model.FontSize
import com.example.testowytestownik.data.model.SettingsState
import com.example.testowytestownik.data.model.SettingsStore
import kotlinx.coroutines.launch

class SettingsManager(private val store: SettingsStore) : ViewModel() {
    var uiState by mutableStateOf(SettingsState())

    init {
        viewModelScope.launch {
            store.loadSettings().collect { loadedSettings ->
                uiState = loadedSettings
            }
        }
    }
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
}