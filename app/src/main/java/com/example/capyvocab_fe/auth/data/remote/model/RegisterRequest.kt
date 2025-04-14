package com.example.capyvocab_fe.auth.data.remote.model

data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String,
)

