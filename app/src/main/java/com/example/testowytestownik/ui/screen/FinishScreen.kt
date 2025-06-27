package com.example.testowytestownik.ui.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.widget.Space
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.media3.common.util.HandlerWrapper.Message
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.navigation.Screen
import org.commonmark.node.Image
import java.io.File
import androidx.core.graphics.createBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun FinishScreen(
    navController: NavController,
    quizName: String,
    questions: Int,
    correct: Int,
    bad: Int,
    timeOfSession: List<Int>
) {

    val context = LocalContext.current
    val message= stringResource(R.string.share)

    val scrollState = rememberScrollState()
    val activity = context as Activity

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.cacheDir, "${System.currentTimeMillis()}.png")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
        }
        return file
    }

    fun shareImage(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, message))
    }


    Surface()
    {
        Box(
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        stringResource(R.string.back_button_desc),
                        modifier = Modifier
                            .clickable
                            {
                                navController.navigate(route = Screen.Menu.route)
                                {
                                    popUpTo(Screen.Menu.route)
                                }
                            }
                            .size(38.dp))
                    Text(
                        text = "${stringResource(R.string.finished)} $quizName",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.size(38.dp))
                }
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
                {
                    val rotation = remember { Animatable(0f) }

                    Box (
                        Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ){
                        LaunchedEffect(Unit) {
                            rotation.animateTo(
                                targetValue = 360f,
                                animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                            )
                        }

                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(R.drawable.finish_testo)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Testo(viron) finished",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxHeight(0.36f)
                                .graphicsLayer {
                                    rotationZ = rotation.value
                                }
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(stringResource(R.string.testo_proud),
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(20.dp))

                    Text("${stringResource(R.string.last_session_time)}:",
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(10.dp))
                    Text("${timeOfSession[0]}:${"%02d".format(timeOfSession[1])}:${"%02d".format(timeOfSession[2])}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.scale(1.75f))


                    Spacer(Modifier.height(20.dp))

                    Text("${stringResource(R.string.all_questions)}:",
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(10.dp))
                    Text("$questions",
                        modifier = Modifier.scale(1.33f))

                    Spacer(Modifier.height(20.dp))


                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = stringResource(R.string.correct_answers) + ":",
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Clip
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "$correct",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.scale(1.33f),
                                color = Color.Green.copy(alpha = 0.7f)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = stringResource(R.string.bad_answers) + ":",
                                textAlign = TextAlign.Center,
                                overflow = TextOverflow.Clip
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = "$bad",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.scale(1.33f),
                                color = Color.Red.copy(alpha = 0.7f)
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    ElevatedButton(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .height(60.dp)
                            .fillMaxWidth(1f),
                        onClick = {
                            navController.navigate(Screen.Quiz.route)
                            {
                                popUpTo(Screen.Menu.route)
                                {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                    { Text(stringResource(R.string.once_again)) }


                    Box(
                        modifier = Modifier.fillMaxWidth()
                    )
                    {
                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.BottomEnd),
                            onClick = {
                                val view = activity.window.decorView
                                val bitmap = createBitmap(view.width, view.height)

                                val locationInWindow = IntArray(2)
                                view.getLocationInWindow(locationInWindow)

                                try {
                                    PixelCopy.request(
                                        activity.window,
                                        Rect(locationInWindow[0], locationInWindow[1],
                                            locationInWindow[0] + view.width,
                                            locationInWindow[1] + view.height),
                                        bitmap,
                                        { result ->
                                            if (result == PixelCopy.SUCCESS) {
                                                val file = saveBitmapToFile(context, bitmap)
                                                shareImage(context, file)
                                            }
                                        },
                                        Handler(Looper.getMainLooper())
                                    )
                                } catch (e: IllegalArgumentException) {
                                    e.printStackTrace()
                                }
                            },
                            content = { Icon(Icons.Filled.Share, "Share testo stats", modifier = Modifier.scale(1.5f))}
                        )
                    }
                }

            }
        }
    }
}