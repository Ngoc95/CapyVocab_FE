package com.example.capyvocab_fe.admin.dashboard.domain.model

data class RevenueWeekPoint(
    val date: String,
    val total: Number
)

data class RevenueMonthPoint(
    val week: String,
    val total: Number
)

data class RevenueYearPoint(
    val month: String,
    val total: Number
)

data class RevenueStats(
    val total: Number,
    val weekly: List<RevenueWeekPoint>,
    val monthly: List<RevenueMonthPoint>,
    val yearly: List<RevenueYearPoint>
)
