package com.example.capyvocab_fe.admin.word.data.remote.model

import com.example.capyvocab_fe.core.network.ApiResponse

data class AudioUploadResult(
    val filename: String,
    val destination: String
)

typealias AudioUploadResponse = ApiResponse<AudioUploadResult>
