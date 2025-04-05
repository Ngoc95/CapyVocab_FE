package com.example.capyvocab_fe.auth.presentation.login_screen

data class LoginViewState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)
