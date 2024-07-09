package com.uiel.dopaminedetox

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun BreathScreen(
    modifier: Modifier,
    navController: NavController,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.breath)
    )
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curValue = animateIntAsState(
        targetValue = if (animationPlayed) 0 else 100,
        animationSpec = tween(
            durationMillis = 1800,
            delayMillis = 0,
        ), label = ""
    )

    LaunchedEffect(true) {
        animationPlayed = true
        delay(1800)
        animationPlayed = false
        delay(2000)
        navController.navigate("think")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Cyan),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight((curValue.value / 100f))
                .background(Color.White),
            content = { },
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(top = 400.dp),
                text = "잠시, 숨을 쉬어보세요",
                fontSize = 30.sp,
            )
            LottieAnimation(
                modifier = Modifier
                    .fillMaxWidth(),
                composition = composition,
                contentScale = ContentScale.Fit,
                alignment = Alignment.BottomCenter
            )
        }
    }
}
