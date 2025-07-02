package com.example.capyvocab_fe.report.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.report.data.model.CreateReportRequest
import com.example.capyvocab_fe.report.data.model.ReportListResponse
import com.example.capyvocab_fe.report.data.model.UpdateReportRequest
import com.example.capyvocab_fe.report.domain.model.Report
import com.example.capyvocab_fe.report.domain.model.ReportType
import com.example.capyvocab_fe.report.domain.model.ReportStatus
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ReportApi {
    @POST("reports")
    suspend fun createReport(
        @Body request: CreateReportRequest
    ): ApiResponse<Report>

    @GET("reports")
    suspend fun getReports(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: ReportType? = null,
        @Query("status") status: ReportStatus? = null
    ): ApiResponse<ReportListResponse>

    @PUT("reports/{id}")
    suspend fun updateReport(
        @Path("id") id: Int,
        @Body body: UpdateReportRequest
    ): ApiResponse<Report>
}