package com.example.capyvocab_fe.admin.user.data.remote.model

data class UpdateUserRequest(
    val username: String?,
    val email: String?,
    val status: String?,
    val roleId: Int?,
    val avatar: String?
)