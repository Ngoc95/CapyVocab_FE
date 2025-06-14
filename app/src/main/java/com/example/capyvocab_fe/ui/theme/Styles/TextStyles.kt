package com.example.capyvocab_fe.ui.theme.Styles

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.ui.theme.MyLightBlue

val LightBlueTextStyle = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    color = MyLightBlue,
    fontStyle = FontStyle.Normal,
    letterSpacing = 0.sp,
    textDecoration = TextDecoration.None,
    fontFamily = FontFamily.Default
)