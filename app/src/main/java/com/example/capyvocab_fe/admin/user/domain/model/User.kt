package com.example.capyvocab_fe.admin.user.domain.model


data class User(
    val id: Int,
    val username: String,
    val email: String,
    val avatar: String?,
    val status: String,
    val streak: Int,
    val lastStudyDate: String?,
    val totalStudyDay: Int,
    val totalLearnedCard: Int,
    val totalMasteredCard: Int,
    val roleId: Int
    //val role: UserRole
)