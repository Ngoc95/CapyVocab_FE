package com.example.capyvocab_fe.ui.theme.Styles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.ui.theme.MyGray


val TextButtonModifier = Modifier
    .padding(8.dp)
    .clip(RoundedCornerShape(8.dp))
    .background(MyGray)
    .drawBehind {
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(0f + 1.5.dp.toPx(), 0f + 1.5.dp.toPx()),
            size = Size(size.width - 3.dp.toPx(), size.height - 5.dp.toPx()),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )
    }
