package com.example.capyvocab_fe.core.network

data class ApiResponse<T>(
    val message: String,
    val statusCode: Int,
    val metaData: T
)