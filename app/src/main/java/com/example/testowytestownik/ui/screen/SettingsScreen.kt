package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.example.testowytestownik.R
import com.example.testowytestownik.data.storage.FontSize
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.SettingsModel

/*
Settings screen is used to modify app settings, and save them as preferences
 */
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsModel: SettingsModel
) {
    val state = settingsModel.uiState
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    stringResource(R.string.back_button_desc),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = Screen.Menu.route) {
                                popUpTo(Screen.Menu.route)
                            }
                        }
                        .size(38.dp)
                )
                Text(
                    text = stringResource(R.string.settings),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(38.dp))
            }

            Text("Font Size")
            DropdownMenuFontSize(
                selected = state.fontSize,
                onSelect = { settingsModel.updateFontSize(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.darkmode))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = state.darkMode,
                    onCheckedChange = {settingsModel.toogleDarkMode(it)}
                )
            }
            Spacer(modifier = Modifier.height(8.dp))




            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.autosave))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = state.autoSave,
                    onCheckedChange = { settingsModel.toogleAutoSave(it) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text("${stringResource(R.string.init_repeats)} ${state.initRepeats}")
            Slider(
                value = state.initRepeats.toFloat(),
                onValueChange = { settingsModel.updateInitRepeats(it.toInt()) },
                valueRange = 0f..10f
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("${stringResource(R.string.extra_repeats)} ${state.extraRepeats}")
            Slider(
                value = state.extraRepeats.toFloat(),
                onValueChange = { settingsModel.updateExtraRepeats(it.toInt()) },
                valueRange = 0f..10f
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("${stringResource(R.string.max_repeats)} ${state.maxRepeats}")
            Slider(
                value = state.maxRepeats.toFloat(),
                onValueChange = { settingsModel.updateMaxRepeats(it.toInt()) },
                valueRange = 0f..10f
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.hardcoremode))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = state.hardcoreMode,
                    onCheckedChange = { settingsModel.toogleHardcoreMode(it) }
                )
            }
        }
    }
}

//Menu for choosing font size preset
@Composable
fun DropdownMenuFontSize(
    selected: FontSize,
    onSelect: (FontSize) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text("Font Size: ${selected.name}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            FontSize.values().forEach { size ->
                DropdownMenuItem(
                    text = { Text(size.name) },
                    onClick = {
                        onSelect(size)
                        expanded = false
                    }
                )
            }
        }
    }
}
