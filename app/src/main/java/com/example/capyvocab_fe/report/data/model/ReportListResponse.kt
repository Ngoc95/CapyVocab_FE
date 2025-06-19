package com.example.capyvocab_fe.report.data.model

import com.example.capyvocab_fe.report.domain.model.Report

data class ReportListResponse(
    val reports: List<Report>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)
