package com.example.capyvocab_fe.admin.dashboard.domain.model

data class RevenuePoint(
    val date: String,
    val amount: Long
)
data class RevenueStats(
    val weekly: List<RevenuePoint>,
    val monthly: List<RevenuePoint>,
    val yearly: List<RevenuePoint>
)
