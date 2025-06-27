package com.example.capyvocab_fe.auth.presentation.forgot_password_screen

data class ForgotPasswordEmailState(
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isEmailSent: Boolean = false
)
