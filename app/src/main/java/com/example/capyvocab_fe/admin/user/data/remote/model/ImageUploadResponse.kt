package com.example.capyvocab_fe.admin.user.data.remote.model

import com.example.capyvocab_fe.core.network.ApiResponse

data class ImageUploadResult(
    val filename: String,
    val destination: String
)

typealias ImageUploadResponse = ApiResponse<List<ImageUploadResult>>
