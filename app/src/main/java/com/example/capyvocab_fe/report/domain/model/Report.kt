package com.example.capyvocab_fe.report.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

enum class ReportType {
    EXERCISES, POST
}

fun ReportType.toDisplayName(): String {
    return when (this) {
        ReportType.EXERCISES -> "Bài tập"
        ReportType.POST -> "Bài viết"
    }
}

enum class ReportStatus {
    PENDING, ACCEPTED, DECLINED
}

fun ReportStatus.toDisplayName(): String {
    return when (this) {
        ReportStatus.PENDING -> "Đang chờ"
        ReportStatus.ACCEPTED -> "Đã duyệt"
        ReportStatus.DECLINED -> "Bỏ qua"
    }
}

data class Report(
    val id: Int,
    val type: ReportType,
    val content: String,
    val status: ReportStatus,
    val targetId: Int,
    val createdAt: String? = null,
    val createdBy: User
)
