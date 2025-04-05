package com.example.capyvocab_fe.auth.data.remote.model

import com.example.capyvocab_fe.core.data.model.UserData

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: UserData
)
