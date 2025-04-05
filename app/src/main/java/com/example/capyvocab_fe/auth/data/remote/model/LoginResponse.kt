package com.example.capyvocab_fe.auth.data.remote.model

import com.example.capyvocab_fe.core.data.model.UserData

data class LoginResponse(
    val message: String,
    val statusCode: Int,
    val metaData: UserData
)
