package com.example.testowytestownik.data.storage

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name="settings")

class SettingsStore(private val context: Context) {
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    private val AUTO_SAVE_KEY = booleanPreferencesKey("auto_save")
    private val FONT_SIZE_KEY = stringPreferencesKey("font_size")
    private val INITIAL_REPEATS_KEY = intPreferencesKey("initial_repeats")
    private val EXTRA_REPEATS_KEY = intPreferencesKey("extra_repeats")
    private val MAXIMUM_REPEATS_KEY = intPreferencesKey("maximum_repeats")
    private val HARDCORE_MODE_KEY = booleanPreferencesKey("hardcore_mode")

    val defaultSettings = SettingsState()

    fun loadSettings(): Flow<SettingsState> = context.dataStore.data
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
        context.dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }
    suspend fun saveAutoSave(enabled: Boolean) {
        context.dataStore.edit { it[AUTO_SAVE_KEY] = enabled }
    }
    suspend fun saveHardcoreMode(enabled: Boolean) {
        context.dataStore.edit { it[HARDCORE_MODE_KEY] = enabled }
    }
    suspend fun saveFontSize(size: FontSize) {
        context.dataStore.edit { it[FONT_SIZE_KEY] = size.name }
    }
    suspend fun saveInitRepeats(value: Int) {
        context.dataStore.edit { it[INITIAL_REPEATS_KEY] = value }
    }
    suspend fun saveExtraRepeats(value: Int) {
        context.dataStore.edit { it[EXTRA_REPEATS_KEY] = value }
    }
    suspend fun saveMaxRepeats(value: Int) {
        context.dataStore.edit { it[MAXIMUM_REPEATS_KEY] = value }
    }
}