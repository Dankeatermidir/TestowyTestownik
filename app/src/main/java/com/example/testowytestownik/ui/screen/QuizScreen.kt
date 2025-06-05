package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.sharp.Done
import androidx.compose.material.icons.twotone.Done
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.navigation.Screen
import com.example.testowytestownik.viewmodel.QuizModel

@Composable
fun QuizScreen(
    navController: NavController
) {
  Surface {
      Box(
          modifier = Modifier.fillMaxSize()
      )
      {
          Column(
              modifier = Modifier
                  .padding(20.dp)
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
                          .size(38.dp)
                  )
                  Text(
                      text = stringResource(R.string.info),
                      fontSize = 30.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.weight(1f),
                      textAlign = TextAlign.Center
                  )
                  Spacer(modifier = Modifier.size(38.dp))
              }
              Text(
                  text = "Rozważmy układ równań różniczkowych du/dt. = Au. Ile wynoszą wartości własne macierzy A, gdy A= [1 2 ; 1 2]",
                  fontWeight = FontWeight.Bold
              )
              var test = listOf("a. 0 oraz (-3)", "b. 1 oraz 2", "c. 0 oraz (3)", "d. 2 oraz 2")
              LazyColumn(
                  modifier = Modifier
                      .weight(1f)
                      .padding(horizontal = 10.dp)
              )
              {
                   items(test.size)
                   {
                       i -> var itemOffset by remember { mutableStateOf(Offset.Zero) }
                       ElevatedButton(
                         modifier = Modifier
                             .padding(vertical = 15.dp)
                             .fillMaxWidth(1f)
                             .height(75.dp),
                         onClick =
                             {
                             navController.navigate(route = Screen.Menu.route)
                             {
                                 popUpTo(Screen.Menu.route)
                             }
                         }
                       )
                       {
                         Icon(Icons.Rounded.Check, test[i])
                         Text(text = test[i])
                       }
                  }
              }
              ElevatedButton(
                  modifier = Modifier
                      .padding(horizontal= 10.dp, vertical = 5.dp)
                      .fillMaxWidth(1f)
                      .height(75.dp),
                  onClick =
                  {

                  }

              ) { Text(text = stringResource(R.string.next)) }

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