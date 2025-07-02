package com.example.capyvocab_fe.report.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.report.domain.model.Report
import com.example.capyvocab_fe.report.domain.model.ReportStatus
import com.example.capyvocab_fe.report.domain.model.ReportType
import com.example.capyvocab_fe.report.domain.model.toDisplayName
import com.example.capyvocab_fe.ui.theme.dimens

@Composable
fun AdminReportScreen(
    state: ReportState,
    onEvent: (ReportEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onEvent(ReportEvent.LoadReports)
    }

    AdminReportScreenContent(
        reports = state.reports,
        reportType = state.reportType,
        isLoading = state.isLoading,
        isEndReached = state.isEndReached,
        selectedStatus = state.selectedStatus,
        onReportTypeChanged = { onEvent(ReportEvent.ReportTypeChanged(it)) },
        onUpdateReportStatus = { reportId, status ->
            onEvent(ReportEvent.UpdateReportStatus(reportId, status))
        },
        onLoadMoreReports = { onEvent(ReportEvent.LoadMoreReports) },
        onStatusFilterChanged = { onEvent(ReportEvent.ReportStatusChanged(it)) }
    )
}

@Composable
fun AdminReportScreenContent(
    reports: List<Report>,
    reportType: ReportType,
    isLoading: Boolean,
    isEndReached: Boolean,
    selectedStatus: ReportStatus,
    onReportTypeChanged: (ReportType) -> Unit,
    onUpdateReportStatus: (Int, ReportStatus) -> Unit,
    onLoadMoreReports: () -> Unit,
    onStatusFilterChanged: (ReportStatus) -> Unit
) {
    val listState = rememberLazyListState()
    var filterMenuExpanded by remember { mutableStateOf(false) }
    val statusOptions = ReportStatus.values()

    // Detect khi cuộn đến gần cuối
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount

                if (lastVisibleItem >= totalItems - 2 && !isLoading && !isEndReached) {
                    onLoadMoreReports()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = ReportType.values().indexOf(reportType)
        ) {
            ReportType.values().forEachIndexed { index, type ->
                Tab(
                    selected = reportType == type,
                    onClick = { onReportTypeChanged(type) },
                    text = { Text(type.toDisplayName()) }
                )
            }
        }

        // Status filter row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { filterMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Lọc trạng thái"
                )
            }
            Text(
                text = selectedStatus.toDisplayName(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 4.dp)
            )
            DropdownMenu(
                expanded = filterMenuExpanded,
                onDismissRequest = { filterMenuExpanded = false }
            ) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status.toDisplayName()) },
                        onClick = {
                            onStatusFilterChanged(status)
                            filterMenuExpanded = false
                        },
                        leadingIcon = {
                            if (selectedStatus == status) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            itemsIndexed(reports) { index, report ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
                    ) {
                        Text(text = "Người tạo: ${report.createdBy.username}")
                        Text(text = "Nội dung: ${report.content}")
                        Text(text = "Trạng thái: ${report.status.toDisplayName()}")

                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Button(
                                onClick = { onUpdateReportStatus(report.id, ReportStatus.ACCEPTED) },
                                enabled = report.status == ReportStatus.PENDING
                            ) {
                                Text("Duyệt")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onUpdateReportStatus(report.id, ReportStatus.DECLINED) },
                                enabled = report.status == ReportStatus.PENDING
                            ) {
                                Text("Bỏ qua")
                            }
                        }
                    }
                }
            }
            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}
