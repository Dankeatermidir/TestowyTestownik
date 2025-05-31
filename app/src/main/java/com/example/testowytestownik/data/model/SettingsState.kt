package com.example.testowytestownik.data.model


data class SettingsState(
    val darkMode: Boolean = false,
    val autoSave: Boolean = true,
    val fontSize: FontSize = FontSize.Medium,

    val initRepeats: Int = 3,
    val extraRepeats: Int = 1,
    val maxRepeats: Int = 3,
    val hardcoreMode: Boolean = false
)

enum class FontSize { Small, Medium, Large }

