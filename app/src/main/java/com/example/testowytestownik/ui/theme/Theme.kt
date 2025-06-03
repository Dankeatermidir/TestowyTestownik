package com.example.testowytestownik.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.testowytestownik.data.storage.FontSize
import com.example.testowytestownik.data.storage.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


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
//    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current
    val darkThemeFlow = context.dataStore.data
        .map { it[booleanPreferencesKey("dark_mode")] ?: false }
    val darkTheme by darkThemeFlow.collectAsState(initial = false)

    val fontSizeFlow: Flow<Float> = context.dataStore.data
        .map { preferences ->
            val storedValue = preferences[stringPreferencesKey("font_size")]
            FontSize.fromName(storedValue).scale
        }
    val fontSize = fontSizeFlow.collectAsState(initial = 1.0f)


    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    val Typography = androidx.compose.material3.Typography(
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16.0f * fontSize.value).sp ,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        labelLarge = MaterialTheme.typography.bodyLarge.copy(
            fontSize = (MaterialTheme.typography.bodyLarge.fontSize.value * fontSize.value).sp
        ),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(
            fontSize = (MaterialTheme.typography.headlineSmall.fontSize.value * fontSize.value).sp
        ),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(
            fontSize = (MaterialTheme.typography.bodyMedium.fontSize.value * fontSize.value).sp
        )
    )

    MaterialTheme (
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}