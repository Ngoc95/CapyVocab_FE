package com.example.capyvocab_fe.admin.user.data.remote.model

data class CreateUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val roleId: Int
)