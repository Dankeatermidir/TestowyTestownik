package com.example.testowytestownik.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.components.YoutubeVideo
import com.example.testowytestownik.ui.components.topText


//screen with useless instructions
@Composable
fun ManualScreen(navController: NavController) {

    val imageTips = listOf(
        R.drawable.dodaj,
        R.drawable.baza,
        R.drawable.edit,
    )

    val textTips = listOf(
        R.string.tip1,
        R.string.tip2,
        R.string.tip3,
    )

    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(pageCount = {
        imageTips.size
    })
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            topText(navController, R.string.manual)

            //display video with "instructions"
            YoutubeVideo("dQw4w9WgXcQ")
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${stringResource(R.string.addingtestowiron)}:",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            //pager with tips
            HorizontalPager(state = pagerState) { page ->
                Image(
                    painter = painterResource(id = imageTips[page]),
                    contentDescription = stringResource(textTips[page]),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(4.dp, Color.Black, RoundedCornerShape(16.dp)),
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(textTips[page]),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


