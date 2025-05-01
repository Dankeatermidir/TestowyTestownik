package com.example.testowytestownik

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun InfoScreen(
    navController: NavController
) {
  Box(
      modifier = Modifier.fillMaxSize()
  ) {
      Column (
          modifier = Modifier
              .padding(vertical = 5.dp, horizontal = 30.dp)
              .fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Top,
      ) {
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
              Icon(Icons.Default.CheckCircle, "time to kys", tint = Color.Black)
              Text(text = "GOT IT!", color = Color.Black)
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