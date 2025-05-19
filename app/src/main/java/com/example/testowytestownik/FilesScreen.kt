package com.example.testowytestownik

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun FilesScreen(
    folderName: String = "MyAppData" // optional: browse a subfolder
) {
    val context = LocalContext.current
    val files = remember {
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) dir.mkdirs()
        dir.listFiles()?.toList() ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Contents of: /data/data/${context.packageName}/files/$folderName",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (files.isEmpty()) {
            Text("No files found in $folderName.")
        } else {
            LazyColumn {
                items(
                    count = files.size,
                    key = { index -> files[index] }, // optional
                    itemContent = { index ->
                        Text(text = files[index].name)
                    }
                )
            }
        }
    }
}
