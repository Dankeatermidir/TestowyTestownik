package com.example.testowytestownik.ui.components

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.core.MarkwonTheme

//compile Markdown
@Composable
fun MarkdownText(
    @RawRes rawResId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    val markdownContent = remember(rawResId) {
        context.resources.openRawResource(rawResId)
            .bufferedReader()
            .use { it.readText() }
    }

    val markwon = remember(colors) {
        Markwon.builder(context)
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    builder
                        //.headingTextColor(colors.onSurface.toArgb())
                        .linkColor(colors.primary.toArgb())
                        .codeTextColor(colors.onSurface.toArgb())
                        .codeBackgroundColor(colors.surface.toArgb())
                }
            })
            .build()
    }

    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextColor(colors.onSurface.toArgb())
            }
        },
        update = { textView ->
            textView.setTextColor(colors.onSurface.toArgb())
            markwon.setMarkdown(textView, markdownContent)
        },
        modifier = modifier
    )
}