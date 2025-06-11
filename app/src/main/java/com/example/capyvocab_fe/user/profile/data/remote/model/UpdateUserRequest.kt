package com.example.capyvocab_fe.user.profile.data.remote.model

data class UpdateUserRequest(
    val username: String,
    val email: String,
    val status: String,
    val avatar: String?
)
