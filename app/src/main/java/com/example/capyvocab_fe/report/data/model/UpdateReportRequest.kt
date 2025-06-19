package com.example.capyvocab_fe.report.data.model

import com.example.capyvocab_fe.report.domain.model.ReportStatus

data class UpdateReportRequest(
    val status: ReportStatus
)
