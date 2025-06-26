package com.example.capyvocab_fe.auth.presentation.forgot_password_screen

data class ForgotPasswordCodeState(
    val code: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val isSuccess: Boolean = false,
    val remainingTime: Long = 300, // 5 phút = 300 giây
    val canResend: Boolean = false
)