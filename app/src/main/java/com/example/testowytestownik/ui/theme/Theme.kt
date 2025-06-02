package com.example.testowytestownik.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.testowytestownik.viewmodel.SettingsManager
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
    surface = SurfaceDark,
    background = BackgroundDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    tertiary = TertiaryLight,
    surface = SurfaceLight,
    background = BackgroundLight


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TestowyTestownikTheme(
    settingsManager: SettingsManager,
//    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = settingsManager.uiState.darkMode
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val Typography = androidx.compose.material3.Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16 * settingsManager.uiState.fontSize.scale).sp ,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        labelLarge = MaterialTheme.typography.bodyLarge.copy(
            fontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * settingsManager.uiState.fontSize.scale).sp
        ),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(
            fontSize = (MaterialTheme.typography.headlineSmall.fontSize.value * settingsManager.uiState.fontSize.scale).sp
        ),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(
            fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * settingsManager.uiState.fontSize.scale).sp
        )
    )

    MaterialTheme (
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}