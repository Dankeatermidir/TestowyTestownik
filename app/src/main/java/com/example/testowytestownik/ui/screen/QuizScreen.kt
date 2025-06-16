package com.example.testowytestownik.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.QueFile
import com.example.testowytestownik.viewmodel.QuizModel
import kotlinx.coroutines.delay
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.testowytestownik.ui.components.YoutubeVideo
import com.example.testowytestownik.viewmodel.YQueFile
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
    val userAns = remember { mutableStateListOf<Int>() }
    val yUserAns = remember { mutableStateListOf<Int>() }
    var err = ""
    var timer = remember { mutableStateOf(listOf(0,0,0)) }

    var answered by remember{ mutableStateOf(false) }
    var wereClickedBefore=false
    val scrollState = rememberScrollState()

    fun getQuestion()
    {
        try
        {
//            thisQuestion="A178"
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
    fun drawImage(name: String, accessibilityDescription: String) {
        val imageFile = File(context.filesDir, "testowniki/$thisQuiz/$name")

//        Log.d("DrawImage", "Loading from: ${imageFile.absolutePath}")
//
//        val fileList = context.filesDir.listFiles()
//        Log.d("FILES", "Zawartość katalogu:")
//        fileList?.forEach {
//            Log.d("FILES", it.name)
//        }

        if (imageFile.exists())
        {
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
        }
        else
        {
            Text("No such picture: \"${context.filesDir}/${imageFile.name}\"|\nCorrect your Testownik in question ${thisQuestion}.txt and read this Testownik's folder.")
        }
    }

    @Composable
    fun placeAudiofile(name: String)
    {
        val audioFile = File(context.filesDir,"/testowniki/$thisQuiz/$name")

        if (audioFile.exists())
        {
            val exoPlayer = remember {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(audioFile.toUri()))
                    prepare()
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    exoPlayer.release()
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally)
            {
                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    ElevatedButton(
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                            .height(75.dp),
                        onClick = {
                        exoPlayer.pause()
                        exoPlayer.seekTo(0)
                        exoPlayer.play()
                    }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "play"
                        )
                        Text(stringResource(R.string.play_audio))
                    }
                    Spacer(Modifier.width(30.dp))
                    ElevatedButton(
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                            .height(75.dp),
                        onClick = {
                        exoPlayer.pause()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "stop"
                        )
                        Text(stringResource(R.string.stop_audio))
                    }
                }
            }

        }
        else
        {
            Text("No such file: ${context.filesDir}/testowniki/$thisQuiz/$name")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PartialBottomSheet(imagesList: List<String>) {
        var showBottomSheet by remember { mutableStateOf(false) }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false,
        )

        Box (
            modifier = Modifier.fillMaxWidth()
        )
        {
            ExtendedFloatingActionButton(
                modifier=Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomEnd),
                onClick = { showBottomSheet = true },
                icon = { Icon(Icons.Filled.Collections, "Multiple images icon") },
                text = { Text(text = stringResource(R.string.show_pictures)) }
            )

            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.fillMaxHeight(),
                    sheetState = sheetState,
                    onDismissRequest = { showBottomSheet = false }
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {
                            items(imagesList.size) { i ->
                                Text("${stringResource(R.string.picture)} ${i+1}:")
                                Spacer(Modifier.height(30.dp))
                                drawImage(quizModel.parseFile(imagesList[i],"img"),"Picture-answer for $i answer")
                                Spacer(Modifier.height(30.dp))
                            }
                        }

                    }
                }
            }
        }

    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun drawY(yQue: YQueFile)
    {
        var noChoiceQueList = Regex("""\{wybór \d+\}""").split(yQue.question)

        val selectionAndHideState = remember {List(yQue.answers.size) { mutableStateOf(0 to false) }}

        val choiceTextList = remember { mutableStateListOf<String>().apply { repeat(yQue.answers.size) { add("") } } }

        FlowRow(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        )
        {
            for (i in 0..noChoiceQueList.size-1)
            {
                if(yUserAns.size<i)
                {
                    yUserAns+=Int.MIN_VALUE
                }

                Text(noChoiceQueList[i])
                if (selectionAndHideState.size > i)
                {
                    val choiceText = choiceTextList[i]
                    Text(
                        text = if (choiceText.length > 0) { choiceText } else {"{${stringResource(R.string.choice)} ${i + 1}}"},
                        modifier = Modifier
                            .clickable { selectionAndHideState[i].value = selectionAndHideState[i].value.copy(second = true) }
                            .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        color = if(!answered) {MaterialTheme.colorScheme.onPrimaryContainer} else { if(yQue.correct[i]==yUserAns[i]){Color.Green} else{Color.Red} }
                    )

                    DropdownMenu(
                        expanded = selectionAndHideState[i].value.second,
                        onDismissRequest = { selectionAndHideState[i].value = selectionAndHideState[i].value.copy(second = false) }
                    )
                    {
                        for(ii in 0 .. yQue.answers[i].size-1)
                        {
                            DropdownMenuItem(text={Text(yQue.answers[i][ii])}, onClick = { selectionAndHideState[i].value = selectionAndHideState[i].value.copy(ii,false); choiceTextList[i] = yQue.answers[i][ii]; yUserAns[i]=ii} )
                            if(ii<yQue.answers[i].size-1)
                            {
                                HorizontalDivider()
                            }
                        }
                    }


                }
            }

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
                if (thisQuiz == "")
                {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(R.string.no_recent),
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
                        Text(text = stringResource(R.string.goto_management))
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                    return@Box
                }
                else
                {
                    if(isReady)
                    {
                        try
                        {
                            getQuestion()
                        }
                        catch (e: Exception)
                        {
                            Text("ERROR: [in question Loading: $e] \n [$err]")
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
                }



                val correct = quizModel.correctAnswersList(que)
                ModalNavigationDrawer(
                    drawerContent = {
                        ModalDrawerSheet {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(scrollState)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            )
                            {
                                Text(stringResource(R.string.stats), modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
                                HorizontalDivider()

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${stringResource(R.string.test_progress)}:", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(text="${stringResource(R.string.all_questions)}:")
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(allQuestions.toString(),
                                    modifier = Modifier
                                        .scale(1.5f)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text="${stringResource(R.string.done_questions)}:")
                                Text(doneQuestions.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text="${stringResource(R.string.to_do_questions)}:")
                                Text(remainingQuestions.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                var quizProgress by remember{ mutableStateOf(0f) }
                                quizProgress = if(allQuestions>0) { (doneQuestions).toFloat()/(allQuestions).toFloat() } else { 0.toFloat() }
                                LinearProgressIndicator(
                                    progress = { quizProgress },
                                    modifier = Modifier.fillMaxWidth(0.8f),
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(20.dp))

                                Text(text="${stringResource(R.string.correct_answers)}:")
                                Text(correctAnswers.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text="${stringResource(R.string.bad_answers)}:")
                                Text(wrongAnswers.toString(),
                                    modifier = Modifier
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                var correctRatio by remember{ mutableStateOf(0f)}
                                correctRatio = if((correctAnswers+wrongAnswers)>0) { (correctAnswers).toFloat()/(correctAnswers+wrongAnswers).toFloat() } else { 0.toFloat() }
                                LinearProgressIndicator(
                                    progress = { correctRatio },
                                    modifier = Modifier.fillMaxWidth(0.8f)
                                )
//                                Text("$correctRatio")
                                Spacer(modifier = Modifier.height(20.dp))
                                HorizontalDivider()
                                Spacer(modifier = Modifier.height(20.dp))
//                                NavigationDrawerItem(
//                                    label = { Text(text = "Poprawnych odpowiedzi:") },
//                                    selected = false,
//                                    onClick = { }
//                                )
                                Text(text = stringResource(R.string.session_duration), fontWeight = FontWeight.Bold)

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
                                        quizModel.resetQuiz(thisQuiz,)
                                        quizModel.resetTimer()
                                    }
                                ) {
                                    Text(stringResource(R.string.reset_progress))
                                }
                                Text(text="$thisQuestion.txt", modifier = Modifier.scale(0.8f))
                                Text(text="R: ${quizModel.currentRepeats}")
                            }
                        }
                    }
                )
                {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    )
                    {

                        if(que.typeCorrect[0]=='X')
                        {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            )
                            {

                                Row()
                                {
                                    if ("[img]" !in que.question && "[yt]" !in que.question && "[aud]" !in que.question) {
                                        Text(
                                            text = que.question,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier
                                                .padding(vertical = 10.dp)
                                        )
                                    }
                                    else
                                    {
                                        var imageVisible by remember { mutableStateOf(true) }
                                        Column(modifier = Modifier.animateContentSize()) {
                                            var whichMedium=""
                                            AnimatedVisibility(visible = imageVisible)
                                            {
                                                if("[img]" in que.question)
                                                {
                                                    whichMedium=stringResource(R.string.image_question)
                                                    drawImage(
                                                        quizModel.parseFile(que.question,"img"),
                                                        "${stringResource(R.string.image_question)} № ${que.question}"
                                                    )
                                                }
                                                else if ("[aud]" in que.question)
                                                {
                                                    whichMedium=stringResource(R.string.audio_question)
                                                    placeAudiofile(quizModel.parseFile(que.question,"aud"))
                                                }
                                                else if ("[yt]" in que.question)
                                                {
                                                    whichMedium=stringResource(R.string.video_question)
                                                    YoutubeVideo(quizModel.parseFile(que.question,"yt"))
                                                }
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically)
                                            {
                                                val descText="${if(!imageVisible){stringResource(R.string.show)} else{stringResource(R.string.hide)}} $whichMedium"
                                                ElevatedButton(onClick = { imageVisible = !imageVisible }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary))
                                                {
                                                    Icon(
                                                        imageVector = if (imageVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                                        contentDescription = descText
                                                    )
                                                    Text(descText)
                                                }
                                            }


                                        }

                                    }
                                }
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                )
                                {
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
                                                text = if("[img]" in que.answers[i]){"${stringResource(R.string.picture)} ${i+1}"} else {que.answers[i]},
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


                            if (que.answers.any { "[img]" in it })
                            {
                                PartialBottomSheet(que.answers)
                            }
                        }

                        else if(que.typeCorrect[0]=='Y')
                        {
                            Column(
                                modifier = Modifier
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            )
                            {
                                drawY(quizModel.getYQuestion(context,thisQuiz,thisQuestion))
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
                                            var areCorrect=false
                                            if(que.typeCorrect[0]=='X')
                                            {
                                                areCorrect=correct.sorted()==userAns.sorted()
                                            }
                                            else
                                            {
                                                areCorrect=quizModel.getYQuestion(context,thisQuiz,thisQuestion).correct==yUserAns.toList()
                                            }

                                            if(areCorrect)
                                            {
                                                quizModel.onCorrectAnswer(thisQuiz,thisQuestion)
                                            }
                                            else
                                            {
                                                quizModel.onWrongAnswer(thisQuiz,thisQuestion)
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
                                            quizModel.resetQuiz(thisQuiz,)
                                            navController.navigate(Screen.Menu.route)
                                        }
                                    }
                                })
                        { Text(text = stringResource(R.string.next)) }
                        Spacer(modifier = Modifier.height(20.dp))


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



