package com.example.capyvocab_fe.report.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.report.data.model.CreateReportRequest
import com.example.capyvocab_fe.report.domain.model.ReportStatus
import com.example.capyvocab_fe.report.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
): ViewModel() {
    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state

    fun onEvent(event: ReportEvent) {
        when (event) {
            is ReportEvent.CreateReport -> createReport()
            is ReportEvent.LoadMoreReports -> loadReports(loadMore = true)
            is ReportEvent.LoadReports -> loadReports()
            is ReportEvent.ReportContentChanged -> _state.update { it.copy(reportContent = event.content) }
            is ReportEvent.ReportTypeChanged -> {
                _state.update {
                    it.copy(
                        reportType = event.type,
                        reports = emptyList(),
                        currentPage = 1,
                        isEndReached = false
                    )
                }
                loadReports()
            }
            is ReportEvent.UpdateReportStatus -> updateReport(event.reportId, event.status)
            is ReportEvent.SetReportData -> {
                _state.update {
                    it.copy(
                        targetId = event.targetId,
                        reportType = event.reportType,
                    )
                }
            }
        }
    }

    private fun createReport() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
            val request = CreateReportRequest(
                type = _state.value.reportType,
                content = _state.value.reportContent,
                targetId = _state.value.targetId ?: 1
            )
            reportRepository.createReport(request)
                .onRight {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Report thành công",
                            reportContent = ""
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Đã xảy ra lỗi",
                            successMessage = ""
                        )
                    }
                }
        }
    }

    private fun loadReports(loadMore: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            val currentType = state.value.reportType
            reportRepository.getReports(page = nextPage, type = currentType)
                .onRight { newReports ->
                    _state.update { state ->
                        val allReports = if (loadMore) state.reports + newReports else newReports
                        state.copy(
                            isLoading = false,
                            reports = allReports,
                            currentPage = nextPage,
                            isEndReached = newReports.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Đã xảy ra lỗi",
                            successMessage = ""
                        )
                    }
                }
        }
    }

    private fun updateReport(reportId: Int, status: ReportStatus) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "", successMessage = "") }
            reportRepository.updateReport(reportId, status)
                .onRight { updatedReport ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Cập nhật báo cáo thành công",
                            reports = it.reports.map { report ->
                                if (report.id == updatedReport.id) updatedReport else report
                            }
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Đã xảy ra lỗi",
                            successMessage = ""
                        )
                    }
                }
        }
    }
}