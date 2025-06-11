package com.example.capyvocab_fe.auth.data.remote.model

data class RefreshResponse(
    val message: String,
    val statusCode: Int,
    val metaData: Map<String, String>
)