package com.example.testowytestownik.data.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.testowytestownik.ui.navigation.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class SettingsStore(private val dataStore: DataStore<Preferences>) {
    val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    val AUTO_SAVE_KEY = booleanPreferencesKey("auto_save")
    val FONT_SIZE_KEY = stringPreferencesKey("font_size")
    val INITIAL_REPEATS_KEY = intPreferencesKey("initial_repeats")
    val EXTRA_REPEATS_KEY = intPreferencesKey("extra_repeats")
    val MAXIMUM_REPEATS_KEY = intPreferencesKey("maximum_repeats")
    val HARDCORE_MODE_KEY = booleanPreferencesKey("hardcore_mode")

    val defaultSettings = SettingsState()

    fun loadSettings(): Flow<SettingsState> = dataStore.data
        .map { prefs ->
            SettingsState(
                darkMode = prefs[DARK_MODE_KEY] ?: defaultSettings.darkMode,
                autoSave = prefs[AUTO_SAVE_KEY] ?: defaultSettings.autoSave,
                fontSize = FontSize.valueOf(prefs[FONT_SIZE_KEY] ?: defaultSettings.fontSize.name),
                initRepeats = prefs[INITIAL_REPEATS_KEY] ?: defaultSettings.initRepeats,
                extraRepeats = prefs[EXTRA_REPEATS_KEY] ?: defaultSettings.extraRepeats,
                maxRepeats = prefs[MAXIMUM_REPEATS_KEY] ?: defaultSettings.maxRepeats
            )
        }
    suspend fun saveDarkMode(enabled: Boolean) {
        dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }
    suspend fun saveAutoSave(enabled: Boolean) {
        dataStore.edit { it[AUTO_SAVE_KEY] = enabled }
    }
    suspend fun saveHardcoreMode(enabled: Boolean) {
        dataStore.edit { it[HARDCORE_MODE_KEY] = enabled }
    }
    suspend fun saveFontSize(size: FontSize) {
        dataStore.edit { it[FONT_SIZE_KEY] = size.name }
    }
    suspend fun saveInitRepeats(value: Int) {
        dataStore.edit { it[INITIAL_REPEATS_KEY] = value }
    }
    suspend fun saveExtraRepeats(value: Int) {
        dataStore.edit { it[EXTRA_REPEATS_KEY] = value }
    }
    suspend fun saveMaxRepeats(value: Int) {
        dataStore.edit { it[MAXIMUM_REPEATS_KEY] = value }
    }
}