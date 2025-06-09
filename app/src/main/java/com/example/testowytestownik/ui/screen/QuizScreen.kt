package com.example.testowytestownik.ui.screen

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.QueFile
import com.example.testowytestownik.viewmodel.QuizModel
import kotlinx.coroutines.delay
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File

@Composable
fun QuizScreen(
    navController: NavController,
    quizModel: QuizModel
) {

    BackHandler {
        quizModel.shouldResetTimer=true
        navController.popBackStack()
    }

    if(quizModel.shouldResetTimer)
    {
        quizModel.shouldResetTimer=false
        quizModel.resetTimer()
    }

    val context=LocalContext.current
    val isReady = quizModel.isReady
    val thisQuiz by quizModel.lastQuiz.collectAsState()
    var thisQuestion = ""
    val allQuestions by quizModel.allQuestions.collectAsState()
    val remainingQuestions by quizModel.remainingQuestions.collectAsState()
    val doneQuestions by quizModel.doneQuestions.collectAsState()
    val wrongAnswers by quizModel.wrongAnswers.collectAsState()
    val correctAnswers by quizModel.correctAnswers.collectAsState()
    var que = QueFile("","",listOf(""))
    var err=""
    var timer = remember { mutableStateOf(listOf(0,0,0)) }
    val userAns = remember { mutableStateListOf<Int>() }
    var answered by remember{ mutableStateOf(false) }
    var wereClickedBefore=false


    fun getQuestion()
    {
        try
        {
//            thisQuestion="066"
            thisQuestion = quizModel.drewQuestion(thisQuiz)
        }
        catch(ee:Exception)
        {
            err=ee.toString()+": in question drawing"
        }
        if(thisQuestion.isNotBlank())
        {
            que = quizModel.getQuestion(context,thisQuiz,thisQuestion)
        }
    }

    @Composable
    fun drawImage(name: String) {
        val imageFile = File(context.filesDir, thisQuiz+"/"+name)

//        Log.d("DrawImage", "Loading from: ${imageFile.absolutePath}")
//
//        val fileList = context.filesDir.listFiles()
//        Log.d("FILES", "Zawartość katalogu:")
//        fileList?.forEach {
//            Log.d("FILES", it.name)
//        }

        if (imageFile.exists()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageFile)
                    .crossfade(true)
                    .build(),
                contentDescription = "que image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(1.0f)
            )
        } else {
            Text("No such picture: \"${context.filesDir}/${imageFile.name}\"|\nCorrect your Testownik in question ${thisQuestion}.txt and readd this Testownik's folder.")
        }
    }

    Surface()
    {
        Box(
            modifier = Modifier.fillMaxSize() )
        {
//            when (val quiz = thisQuiz) {
//                "" -> {
//                    // Ładowanie (dane jeszcze nie nadeszły)
//                    CircularProgressIndicator()
//                }
//                else -> {
//                    // Ekran główny (dane są już dostępne)
//                    Text("Ostatni quiz: $quiz")
//                }
//            }
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
                horizontalArrangement = Arrangement.SpaceBetween )
                {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        stringResource(R.string.back_button_desc),
                        modifier = Modifier
                            .clickable
                            {
                                navController.navigate(route = Screen.Menu.route)
                                {
                                    quizModel.shouldResetTimer=true
                                    popUpTo(Screen.Menu.route)
                                }
                            }
                            .size(38.dp) )
                    Text(
                        text = thisQuiz,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center )
                    Spacer(modifier = Modifier.size(38.dp))
                }
                if(isReady)
                {
                    try
                    {
                        getQuestion()
                    }
                    catch (e: Exception)
                    {
                        if (thisQuiz == "")
                        {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = "Nie otwarłeś wcześniej testownika",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            ElevatedButton(
                                modifier = Modifier
                                    .padding(vertical = 15.dp)
                                    .fillMaxWidth(1f)
                                    .height(75.dp),
                                onClick =
                                    {
                                        quizModel.shouldResetTimer = true
                                        navController.navigate(Screen.Mgmt.route) {
                                            popUpTo(Screen.Menu.route) {
                                                inclusive = false // saves Menu
                                            }
                                            launchSingleTop = true // in advance
                                        }
                                    })
                            {
                                Text(text = "Przejdź do Twoich Testowników")
                            }
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            )
                        } else
                        {
                            Text("ERROR: [in question Loading: $e] \n [$err]")
                        }
                        return@Box
                    }
                }
                else
                {
                    Column (horizontalAlignment = Alignment.CenterHorizontally)
                    {
                        Row (verticalAlignment = Alignment.CenterVertically)
                        {
                            CircularProgressIndicator()
                        }
                    }
                    return@Box
                }


                val correct = quizModel.correctAnswersList(que)
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Statystyki", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                                HorizontalDivider()

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "Postęp Testownika:", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text="Wszystkich pytań w testowniku:")
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(allQuestions.toString(),
                                    modifier = Modifier
                                        .scale(1.5f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text="Do opanowania:")
                                Text(remainingQuestions.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text="Opanowanych:")
                                Text(doneQuestions.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                var correctRatio by remember{ mutableStateOf(if(allQuestions>0) { (doneQuestions/allQuestions).toFloat() } else { 0.toFloat() }) }
                                LinearProgressIndicator(
                                    progress = { correctRatio },
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(20.dp))

                                Text(text="Poprawnych odpowiedzi:")
                                Text(correctAnswers.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text="Błędnych odpowiedzi:")
                                Text(wrongAnswers.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                var quizProgress by remember{ mutableStateOf(if((correctAnswers+wrongAnswers)>0) { (correctAnswers/(correctAnswers+wrongAnswers)).toFloat() } else { 0.toFloat() }) }
                                LinearProgressIndicator(
                                    progress = { quizProgress },
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(20.dp))
//                                NavigationDrawerItem(
//                                    label = { Text(text = "Poprawnych odpowiedzi:") },
//                                    selected = false,
//                                    onClick = { }
//                                )
                                Text(text = "Czas sesji", fontWeight = FontWeight.Bold)

                                LaunchedEffect(Unit) {
                                    while (true) {
                                        delay(1000)
                                        timer.value = quizModel.getElapsedTime()
                                    }
                                }

                                Text(text="${timer.value[0]}:${"%02d".format(timer.value[1])}:${"%02d".format(timer.value[2])}")
                                ElevatedButton(
                                    modifier = Modifier
                                        .padding(vertical = 15.dp)
                                        .fillMaxWidth(0.8f)
                                        .height(75.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                    onClick = {
                                        quizModel.resetQuiz(thisQuiz,2)
                                        quizModel.resetTimer()
                                    }
                                ) {
                                    Text("Zresetuj postęp")
                                }
                                Text(text="$thisQuestion.txt", modifier = Modifier.scale(0.8f))
                            }
                        }
                    }
                )
                {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    )
                    {

                        if ("[img]" !in que.question) {
                            Text(
                                text = que.question,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        else
                        {
                            drawImage(quizModel.parseImgageName(que.question))
                        }
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp)
                        )
                        {
                            if ("[img]" !in que.answers) {
                                items(que.answers.size) { i ->
                                    val isSelected = i in userAns

                                    ElevatedButton(
                                        modifier = Modifier
                                            .padding(vertical = 15.dp)
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .heightIn(min = 75.dp),
                                        onClick = {
                                            if (isSelected) {
                                                userAns.remove(i)
                                            } else {
                                                userAns.add(i)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                            else MaterialTheme.colorScheme.primary,

                                            disabledContainerColor = if (i in correct && i in userAns) Color.Green.copy(
                                                alpha = 0.5f
                                            )
                                            else {
                                                if (i in correct && i !in userAns) Color.Yellow.copy(
                                                    alpha = 0.5f
                                                )
                                                else {
                                                    if (i !in correct && i in userAns) {
                                                        Color.Red.copy(alpha = 0.5f)
                                                    } else {
                                                        Color.Gray.copy(alpha = 0.5f)
                                                    }
                                                }
                                            }
                                        ),
                                        enabled = !answered
                                    )
                                    {
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Icon(
                                            if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.Check,
                                            contentDescription = null
                                        )
                                        Spacer(
                                            modifier = Modifier.fillMaxWidth()
                                                .weight(1f)
                                        )
                                        Text(
                                            text = que.answers[i],
                                            softWrap = true,
                                            overflow = TextOverflow.Visible
                                        )
                                        Spacer(
                                            modifier = Modifier.fillMaxWidth()
                                                .weight(1f)
                                        )
                                    }
                                }
                                answered = false
                            }

                        }

                        ElevatedButton(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .fillMaxWidth(1f)
                                .height(75.dp),
                            onClick =
                                {
                                    if (!wereClickedBefore) {
                                        answered = true
                                        wereClickedBefore = true
                                    } else {

                                        if(remainingQuestions>0)
                                        {
                                            if(correct.sorted()==userAns.sorted())
                                            {
                                                quizModel.onCorrectAnswer(thisQuiz,thisQuestion)
                                            }
                                            else
                                            {
                                                quizModel.onWrongAnswer(thisQuiz,thisQuestion,1,4)
                                            }
                                            navController.navigate(Screen.Quiz.route) {
                                                popUpTo(Screen.Menu.route) {
                                                    inclusive = false
                                                }
                                                launchSingleTop = true
                                            }
                                        }
                                        else
                                        {
                                            quizModel.resetQuiz(thisQuiz,2)
                                            navController.navigate(Screen.Menu.route)
                                        }
                                    }
                                })
                        { Text(text = stringResource(R.string.next)) }
                    }
                }
            }
        }
    }
}

/*
@Composable
@Preview(showBackground = true)
fun InfoScreenPreview(){
    InfoScreen()
}

 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet() {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { showBottomSheet = true }
        ) {
            Text("Display partial bottom sheet")
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Text(
                    "Swipe up to open sheet. Swipe down to dismiss.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}


