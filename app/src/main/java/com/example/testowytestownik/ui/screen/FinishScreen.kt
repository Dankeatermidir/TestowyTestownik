package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.navigation.Screen
import java.io.File

@Composable
fun FinishScreen(
    navController: NavController,
    quizName: String,
    questions: Int,
    correct: Int,
    bad: Int,
    Time: List<Int>
) {

    val context = LocalContext.current

    @Composable
    fun drawImage(name: String, accessibilityDescription: String) {
        val imageFile = File(context.filesDir, "testowniki/" + quizName + "/" + name)

        if (imageFile.exists()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageFile)
                    .crossfade(true)
                    .build(),
                contentDescription = accessibilityDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(1.0f)
            )
        } else {
            Text("Pudzian is missing. If you see it, you should be afraid. The fortune is against you. You will fail at test if you won't return Pudzian back to the app :c")
        }
    }



    Surface()
    {
        Box(
            modifier = Modifier.fillMaxSize()
        )
        {
            Column(
                modifier = Modifier
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

            }
        }
    }
}