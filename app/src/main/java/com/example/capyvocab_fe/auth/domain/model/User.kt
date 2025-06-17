package com.example.capyvocab_fe.auth.domain.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val avatar: String?,
    val balance: Double? = null,
    val roleId: Int
)