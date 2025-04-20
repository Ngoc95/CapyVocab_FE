package com.example.capyvocab_fe.admin.user.data.remote.model

data class UpdateUserRequest(
    val username: String,
    val email: String,
    //val password: String?, // nullable để chỉ gửi khi cần
    val status: String,
    val roleId: Int
)