package com.example.testowytestownik.ui.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.QuizModel
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(
    navController: NavController,
    quizModel: QuizModel
) {
    val context=LocalContext.current
    val thisQuiz by quizModel.lastQuiz.collectAsState()

    val userAns = remember { mutableStateListOf<Int>() }
    var answered by remember{ mutableStateOf(false) }
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
                if(thisQuiz=="")
                {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth() )
                    Text(
                        text="Nie otwierałeś wcześniej żadnego testownika.",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center )
                    Spacer(modifier = Modifier.size(10.dp))
                    ElevatedButton(
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                            .fillMaxWidth(1f)
                            .height(75.dp),
                        onClick =
                        {
                            navController.navigate(Screen.Mgmt.route) {
                                popUpTo(Screen.Menu.route) {
                                    inclusive = false // saves Menu
                                }
                                launchSingleTop = true // in advance
                            }
                        } )
                    {
                        Text(text = "Przejdź do Twoich Testowników")
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth() )
                    PartialBottomSheet()
                    SideDrawer()
                    return@Box
                }
                val que = quizModel.getQuestion(context,thisQuiz,"1")//quizModel.drawQuestion(state.lastQuiz))
                val correct = quizModel.correctAnswersList(que)
                if("[img]" !in que.question)
                {
                    Text(
                        text = que.question,
                        fontWeight = FontWeight.Bold )
                }
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp) )
                {
                    if ("[img]" !in que.answers)
                    {
                        items(que.answers.size) { i ->
                            val isSelected = i in userAns

                            ElevatedButton(
                                modifier = Modifier
                                    .padding(vertical = 15.dp)
                                    .fillMaxWidth()
                                    .height(75.dp),
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

                                    disabledContainerColor = if (i in correct && i in userAns) Color.Green.copy(alpha = 0.5f)
                                    else {
                                        if (i in correct) Color.Yellow.copy(alpha = 0.5f)
                                        else Color.Red.copy(alpha = 0.5f) }
                                ),
                                enabled = !answered
                                )
                            {
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    if (isSelected) Icons.Rounded.CheckCircle  else Icons.Rounded.Check,
                                    contentDescription = null)
                                Spacer(modifier = Modifier.fillMaxWidth()
                                    .weight(1f))
                                Text(text = que.answers[i])
                                Spacer(modifier = Modifier.fillMaxWidth()
                                    .weight(1f))
                            }
                        }
                        answered=false
                    }

                }
                ElevatedButton(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                        .fillMaxWidth(1f)
                        .height(75.dp),
                        onClick =
                        {
                            answered = true
                        } )
                { Text(text = stringResource(R.string.next)) }
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

@Composable
fun SideDrawer()
{
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("Drawer title", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Drawer Item") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                // ...other drawer items
            }
        }
    ) {
        // Screen content
    }
}