package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import com.example.testowytestownik.ui.navigation.Screen

@Composable
fun SettingsScreen(navController: NavController) {
    var darkTheme by remember { mutableStateOf(false) }
    var autosave by remember { mutableStateOf(true) }
    var statistics by remember { mutableStateOf(true) }
    var hardcore by remember { mutableStateOf(false) }

    var fontSize by remember { mutableStateOf(0.5f) }
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tryb ciemny")
                Switch(
                    checked = darkTheme,
                    onCheckedChange = { darkTheme = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Autozapis")
                Switch(
                    checked = autosave,
                    onCheckedChange = { autosave = it }
                )
            }
            Column {
                Text("Rozmiar czcionki")
                Slider(
                    value = fontSize,
                    onValueChange = { fontSize = it }
                )
                Text(text = kotlin.math.floor((fontSize*24+5)).toString())
            }
        }
    }
}