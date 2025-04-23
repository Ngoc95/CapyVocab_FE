package com.example.capyvocab_fe.admin.user.data.remote.model

data class ApiResponse<T>(
    val message: String,
    val statusCode: Int,
    val metaData: T
)