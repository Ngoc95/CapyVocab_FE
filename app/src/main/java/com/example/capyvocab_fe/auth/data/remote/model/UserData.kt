package com.example.capyvocab_fe.auth.data.remote.model

import com.example.capyvocab_fe.core.data.model.RoleData

data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val avatar: String?,
    val role: RoleData
)