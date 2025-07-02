package com.example.capyvocab_fe.report.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.report.data.model.CreateReportRequest
import com.example.capyvocab_fe.report.domain.model.Report
import com.example.capyvocab_fe.report.domain.model.ReportStatus
import com.example.capyvocab_fe.report.domain.model.ReportType

interface ReportRepository {
    suspend fun createReport(request: CreateReportRequest): Either<AppFailure, Report>
    suspend fun getReports(
        page: Int = 1,
        limit: Int = 10,
        type: ReportType? = null,
        status: ReportStatus? = null
    ): Either<AppFailure, List<Report>>
    suspend fun updateReport(
        reportId: Int,
        status: ReportStatus
    ): Either<AppFailure, Report>
}