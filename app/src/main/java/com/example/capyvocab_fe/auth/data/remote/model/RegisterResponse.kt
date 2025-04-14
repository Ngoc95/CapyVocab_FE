package com.example.capyvocab_fe.auth.data.remote.model

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val metaData: RegisterMetaData
)
data class RegisterMetaData(
    val accessToken: String,
    val refreshToken: String
)
