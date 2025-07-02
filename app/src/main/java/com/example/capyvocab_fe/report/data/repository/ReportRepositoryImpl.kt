package com.example.capyvocab_fe.report.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.report.data.model.CreateReportRequest
import com.example.capyvocab_fe.report.data.model.UpdateReportRequest
import com.example.capyvocab_fe.report.data.remote.ReportApi
import com.example.capyvocab_fe.report.domain.model.Report
import com.example.capyvocab_fe.report.domain.model.ReportStatus
import com.example.capyvocab_fe.report.domain.model.ReportType
import com.example.capyvocab_fe.report.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportApi: ReportApi
): ReportRepository {
    override suspend fun createReport(request: CreateReportRequest): Either<AppFailure, Report> {
        return Either.catch {
            reportApi.createReport(request).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getReports(
        page: Int,
        limit: Int,
        type: ReportType?,
        status: ReportStatus?
    ): Either<AppFailure, List<Report>> {
        return Either.catch {
            reportApi.getReports(page, limit, type, status).metaData.reports
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun updateReport(
        reportId: Int,
        status: ReportStatus
    ): Either<AppFailure, Report> {
        return Either.catch {
            reportApi.updateReport(reportId, UpdateReportRequest(status)).metaData
        }.mapLeft { it.toAppFailure() }
    }
}