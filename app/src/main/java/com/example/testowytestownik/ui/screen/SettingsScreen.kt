package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.data.storage.FontSize
import com.example.testowytestownik.ui.components.topText
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
    var address by remember { mutableStateOf(state.bzztmachenAddress) }
    val response = settingsModel.response.collectAsState()
    val scrollState = rememberScrollState()

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            topText(navController, R.string.settings)

            Text(stringResource(R.string.fontsize))
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
                    onCheckedChange = { settingsModel.toogleDarkMode(it) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text("${stringResource(R.string.init_repeats)}: ${state.initRepeats}")
            Slider(
                value = state.initRepeats.toFloat(),
                onValueChange = { settingsModel.updateInitRepeats(it.toInt()) },
                valueRange = 1f..10f
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("${stringResource(R.string.extra_repeats)}: ${state.extraRepeats}")
            Slider(
                value = state.extraRepeats.toFloat(),
                onValueChange = { settingsModel.updateExtraRepeats(it.toInt()) },
                valueRange = 0f..10f
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("${stringResource(R.string.max_repeats)}: ${state.maxRepeats}")
            Slider(
                value = state.maxRepeats.toFloat(),
                onValueChange = { settingsModel.updateMaxRepeats(it.toInt()) },
                valueRange = 1f..10f
            )
            //time to BZZZT!
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.hardcoremode))
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = state.hardcoreMode,
                    onCheckedChange = { settingsModel.toogleHardcoreMode(it) }
                )
            }
            if (state.hardcoreMode) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    //Address to enter
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.65f),
                        value = address,
                        onValueChange = {
                            address = it
                            settingsModel.updateBzztMachenAddress(address)
                        },
                        label = { Text(stringResource(R.string.info_mdns)) },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    // button to test
                    Button(
                        onClick = {
                            settingsModel.quickTest(
                                address = address,
                                player = state.bzztmachenPlayer
                            )
                        }
                    ) {
                        Text(stringResource(R.string.test_config))
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                // player to select
                Row(
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${stringResource(R.string.player)}:")
                    (1..5).forEach { number ->
                        val isSelected = number == state.bzztmachenPlayer
                        Button(
                            onClick = { settingsModel.updateBzztmachenPlayer(number) },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(text = number.toString())
                        }
                    }
                }
                Text(response.value)
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
            Text("${stringResource(R.string.fontsize)}: ${selected.name}")
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
