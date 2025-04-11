package com.example.capyvocab_fe.auth.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val avatar: String?,
    //val role: UserRole
)