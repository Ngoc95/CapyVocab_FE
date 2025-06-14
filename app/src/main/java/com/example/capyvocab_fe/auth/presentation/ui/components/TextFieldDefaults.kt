package com.example.capyvocab_fe.auth.presentation.ui.components

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun defaultTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        cursorColor = Color.Black,
        unfocusedBorderColor = Color.Gray,
        focusedBorderColor = Color(0xFF0866FF),
        focusedTextColor = Color.Black,
        focusedLabelColor = Color.Black,
        unfocusedContainerColor = Color.White,
        focusedContainerColor = Color.White,
        unfocusedPlaceholderColor = Color.Gray
    )
}