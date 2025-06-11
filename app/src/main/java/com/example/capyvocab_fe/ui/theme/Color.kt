package com.example.capyvocab_fe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF1C1E21) //Dark Background
val Blue = Color(0xFF1877F2) //Primary
val White = Color(0xFFFCFAF9) //Light Background
val MyLightBlue = Color(0xFF1CB0F6)
val DarkRed = Color(0xFFC30052) //Dark Error
val LightRed = Color(0xFFFF84B7)
val MyGray = Color(0xd5d5d5d5)

val LightBlack = Color(0xFF3A3B3C) //Dark Surface


val ColorScheme.navBarBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF383838) else Color(0xFFFCFAF9)