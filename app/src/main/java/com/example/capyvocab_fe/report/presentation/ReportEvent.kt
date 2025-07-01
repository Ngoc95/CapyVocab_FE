package com.example.capyvocab_fe.report.presentation

import com.example.capyvocab_fe.report.domain.model.ReportStatus
import com.example.capyvocab_fe.report.domain.model.ReportType

sealed class ReportEvent {
    object CreateReport : ReportEvent()
    data class ReportContentChanged(val content: String) : ReportEvent()
    data class ReportTypeChanged(val type: ReportType) : ReportEvent()
    data class SetReportData(val targetId: Int, val reportType: ReportType) : ReportEvent()
    object LoadReports : ReportEvent()
    object LoadMoreReports : ReportEvent()
    data class UpdateReportStatus(
        val reportId: Int,
        val status: ReportStatus
    ) : ReportEvent()
}
