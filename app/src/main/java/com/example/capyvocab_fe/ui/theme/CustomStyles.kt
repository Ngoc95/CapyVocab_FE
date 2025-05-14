package com.example.capyvocab_fe.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val TextButtonModifier = Modifier
    .padding(8.dp)
    .border(1.dp, Color.Blue, RoundedCornerShape(8.dp))
    .background(Color.Transparent)
    .clip(RoundedCornerShape(8.dp))