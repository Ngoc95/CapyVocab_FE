package com.example.capyvocab_fe.report.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

enum class ReportType {
    EXERCISES, POST
}

enum class ReportStatus {
    PENDING, ACCEPTED, DECLINED
}

data class Report(
    val id: Int,
    val type: ReportType,
    val content: String,
    val status: ReportStatus,
    val createdAt: String? = null,
    val createdBy: User
)
