package com.example.capyvocab_fe.profile.domain.model

import com.example.capyvocab_fe.core.data.model.RoleData
import java.util.Date

data class UserProfile(
    val id: Int,
    val email: String,
    val username: String,
    val avatar: String?,
    val status: String,
    val streak: Int,
    val lastStudyDate: Date?,
    val totalStudyDay: Int,
    val role: RoleData,
    val balance: Double
)