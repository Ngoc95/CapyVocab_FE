package com.example.capyvocab_fe.auth.data.remote.model

data class LoginResponse(
    val message: String,
    val statusCode: Int,
    val metaData: LoginMetaData
)

data class LoginMetaData(
    val user: UserData,
    val accessToken: String,
    val refreshToken: String
)