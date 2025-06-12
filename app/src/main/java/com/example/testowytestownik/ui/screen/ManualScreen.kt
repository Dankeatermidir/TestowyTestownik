package com.example.testowytestownik.ui.screen

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.testowytestownik.R
import com.example.testowytestownik.ui.components.YoutubeVideo
import com.example.testowytestownik.ui.navigation.Screen
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    stringResource(R.string.back_button_desc),
                    modifier = Modifier
                        .clickable {
                            navController.navigate(route = Screen.Menu.route) {
                                popUpTo(Screen.Menu.route)
                            }
                        }
                        .size(38.dp)
                )
                Text(
                    text = stringResource(R.string.manual),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(38.dp))


            }
            YoutubeVideo("dQw4w9WgXcQ")
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${stringResource(R.string.addingtestowiron)}:",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
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


