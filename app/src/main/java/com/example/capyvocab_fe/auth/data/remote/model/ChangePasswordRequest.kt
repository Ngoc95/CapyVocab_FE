package com.example.capyvocab_fe.auth.data.remote.model

data class ChangePasswordRequest(
    val email: String,
    val newPassword: String)
