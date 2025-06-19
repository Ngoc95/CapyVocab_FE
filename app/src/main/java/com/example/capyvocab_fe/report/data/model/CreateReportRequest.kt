package com.example.capyvocab_fe.report.data.model

import com.example.capyvocab_fe.report.domain.model.ReportType

data class CreateReportRequest(
    val type: ReportType,
    val content: String
)
