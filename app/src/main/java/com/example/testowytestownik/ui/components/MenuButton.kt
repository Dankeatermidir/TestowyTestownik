package com.example.testowytestownik.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun MenuButton(name:String, icon: ImageVector, onclick: () -> Unit) {
    ElevatedButton(
        modifier = Modifier
            .padding(vertical = 15.dp)
            .fillMaxWidth(1f)
            .height(75.dp),
        onClick = onclick

    ) {
        Icon(icon, name)
        Text(text = name)
    }
}
