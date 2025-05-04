package com.example.testowytestownik

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InfoScreen(
    navController: NavController
) {
  Surface {
      Box(
          modifier = Modifier.fillMaxSize()
      ) {
          Column(
              modifier = Modifier
                  .fillMaxSize(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Top,
          ) {
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
              ) {
                  Icon(
                      Icons.Default.KeyboardArrowLeft,
                      "back button",
                      modifier = Modifier
                          .clickable {
                              navController.navigate(route = Screen.Menu.route) {
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
                  text = "KYS!",
                  color = Color.Red,
                  fontSize = 50.sp,
                  fontWeight = FontWeight.Bold
              )
              ElevatedButton(
                  modifier = Modifier
                      .padding(vertical = 15.dp)
                      .fillMaxWidth(1f)
                      .height(75.dp),
                  onClick = {
                      navController.navigate(route = Screen.Menu.route) {
                          popUpTo(Screen.Menu.route)
                      }
                  }
              ) {
                  Icon(Icons.Default.CheckCircle, "time to kys")
                  Text(text = "GOT IT!")
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