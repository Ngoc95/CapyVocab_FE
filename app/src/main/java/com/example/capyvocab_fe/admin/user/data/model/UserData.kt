package com.example.capyvocab_fe.admin.user.data.model

data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val fullName: String,
    val avatar: String?,
    val status: String
)
