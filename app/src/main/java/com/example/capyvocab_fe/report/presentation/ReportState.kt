package com.example.capyvocab_fe.report.presentation

import com.example.capyvocab_fe.report.domain.model.Report
import com.example.capyvocab_fe.report.domain.model.ReportType
import com.example.capyvocab_fe.report.domain.model.ReportStatus

data class ReportState(
    val reports: List<Report> = emptyList(),
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = "",
    val reportContent: String = "",
    val reportType: ReportType = ReportType.EXERCISES,
    val targetId: Int? = null,
    val selectedStatus: ReportStatus = ReportStatus.PENDING,
)
