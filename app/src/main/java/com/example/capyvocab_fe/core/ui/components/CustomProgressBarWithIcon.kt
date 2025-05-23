package com.example.capyvocab_fe.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.capyvocab_fe.R

@Composable
fun CustomProgressBarWithIcon(
    progress: Float, // từ 0f đến 1f
    modifier: Modifier = Modifier
) {
    val barHeight = 25.dp
    val iconSize = 40.dp

    BoxWithConstraints(
        modifier = modifier
            .height(iconSize)
    ) {
        val maxWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val iconSizePx = with(LocalDensity.current) { iconSize.toPx() }
        val iconOffsetPx = (progress.coerceIn(0f, 1f) * maxWidthPx).toInt() - (iconSizePx / 2).toInt()

        // Thanh nền
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .height(barHeight)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFE6EAEB))
        )

        // Thanh tiến độ
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                .height(barHeight)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFFFC107))
        )

        // Icon
        Image(
            painter = painterResource(id = R.drawable.progress_bar_ic),
            contentDescription = "Progress Icon",
            modifier = Modifier
                .offset { IntOffset(iconOffsetPx, 0) }
                .size(iconSize)
                .zIndex(1f)
                .align(Alignment.CenterStart)
        )
    }
}
