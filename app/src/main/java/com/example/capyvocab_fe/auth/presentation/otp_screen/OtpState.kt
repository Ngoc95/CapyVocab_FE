package com.example.capyvocab_fe.auth.presentation.otp_screen

data class OtpState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)