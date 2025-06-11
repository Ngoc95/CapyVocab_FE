package com.example.capyvocab_fe.user.profile.domain.model

import java.util.Date

data class ProfileUser (
    val id: Int,
    val email: String,
    val username: String,
    val avatar: String?,
    val status: String,
    val streak: Int,
    val lastStudyDate: Date?,
    val totalStudyDay: Int,
)