package com.example.testowytestownik.data.storage

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

//Recursively copy folder with all it's contents to internal storage
fun copyFilesToInternalStorage(context: Context, uri: Uri) {
    val contentResolver = context.contentResolver
    val pickedFolder = DocumentFile.fromTreeUri(context, uri)
    var folderName = Random.nextInt(0,999999999).toString()
    if (pickedFolder.name != null) { //if folder name is null, name is random numbers
        folderName = pickedFolder.name
    }
    val targetDir = File(context.filesDir, folderName).apply { mkdirs() }

    pickedFolder?.listFiles()?.forEach { file ->
        if (file.isFile) {
            val inputStream = contentResolver.openInputStream(file.uri)
            val outFile = File(targetDir, file.name ?: "unknown_file")
            val outputStream = FileOutputStream(outFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }
    }
}