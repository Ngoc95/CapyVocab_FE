package com.example.capyvocab_fe.admin.user.data.model

import com.example.capyvocab_fe.core.data.model.RoleData

data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val avatar: String?,
    val status: String,
    val streak: Int,
    val lastStudyDate: String?,
    val totalStudyDay: Int,
    val role: RoleData
)