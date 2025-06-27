package com.example.capyvocab_fe.report.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.core.ui.components.SnackbarType
import com.example.capyvocab_fe.report.domain.model.ReportType
import com.example.capyvocab_fe.ui.theme.dimens
import kotlinx.coroutines.delay

@Composable
fun UserReportScreen(
    state: ReportState,
    onEvent: (ReportEvent) -> Unit,
    navController: NavController
) {
    var visibleSuccess by remember { mutableStateOf("") }
    var visibleError by remember { mutableStateOf("") }

    // Show success message for 3 seconds
    LaunchedEffect(state.successMessage) {
        if (state.successMessage.isNotEmpty()) {
            visibleSuccess = state.successMessage
            delay(2000)
            visibleSuccess = ""
        }
    }
    // Show error message for 3 seconds
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(2000)
            visibleError = ""
        }
    }

    FocusComponent {
        UserReportScreenContent(
            reportType = state.reportType,
            reportContent = state.reportContent,
            isLoading = state.isLoading,
            successMessage = visibleSuccess,
            errorMessage = visibleError,
            onBack = { navController.popBackStack() },
            onReportTypeChanged = { onEvent(ReportEvent.ReportTypeChanged(it)) },
            onReportContentChanged = { onEvent(ReportEvent.ReportContentChanged(it)) },
            onCreateReport = { onEvent(ReportEvent.CreateReport) }
        )
    }
}

@Composable
fun UserReportScreenContent(
    reportType: ReportType,
    reportContent: String,
    isLoading: Boolean,
    successMessage: String,
    errorMessage: String,
    onBack: () -> Unit,
    onReportTypeChanged: (ReportType) -> Unit,
    onReportContentChanged: (String) -> Unit,
    onCreateReport: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(MaterialTheme.dimens.medium1).clickable(onClick = onBack)
            )
            Spacer(Modifier.width(MaterialTheme.dimens.small1))
            Text("Báo cáo vi phạm", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        // Chọn loại báo cáo
        Row {
            ReportType.values().forEach { type ->
                FilterChip(
                    selected = reportType == type,
                    onClick = { onReportTypeChanged(type) },
                    label = { Text(type.name) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        // Nội dung báo cáo
        OutlinedTextField(
            value = reportContent,
            onValueChange = { onReportContentChanged(it) },
            label = { Text("Nội dung báo cáo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

        // Nút gửi
        Button(
            onClick = onCreateReport,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text("Gửi báo cáo", style = MaterialTheme.typography.titleMedium)
            }
        }

        OverlaySnackbar(message = successMessage, type = SnackbarType.Success)
        OverlaySnackbar(message = errorMessage, type = SnackbarType.Error)
    }
}