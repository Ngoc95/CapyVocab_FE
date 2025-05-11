package com.example.capyvocab_fe.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color

@Composable
fun RippleOverlay(onFinished: () -> Unit) {
    val animationProgress = remember { Animatable(0f) }

    LaunchedEffect(true) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                drawCircle(
                    color = Color(0xFF8Fd9FF).copy(alpha = (1 - animationProgress.value) * 0.2f),
                    radius = size.maxDimension * animationProgress.value
                )
            }
    )
}