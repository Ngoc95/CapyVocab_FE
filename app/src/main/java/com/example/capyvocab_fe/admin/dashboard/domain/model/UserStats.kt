package com.example.capyvocab_fe.admin.dashboard.domain.model

data class UserPoint(
    val date: String,
    val count: Int
)
data class UserStats(
    val total: Int,
    val newUsers7Days: List<UserPoint>,
    val newUsers30Days: List<UserPoint>,
    val activeUsers7Days: List<UserPoint>,
    val activeUsers30Days: List<UserPoint>
)