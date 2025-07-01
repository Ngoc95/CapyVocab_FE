package com.example.capyvocab_fe.payout.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.payout.domain.model.Payout
import com.example.capyvocab_fe.payout.domain.model.PayoutStatus
import com.example.capyvocab_fe.payout.presentation.components.AdminPayoutCard
import com.example.capyvocab_fe.ui.theme.dimens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminPayoutScreen(
    viewModel: PayoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(PayoutEvent.LoadPayouts)
    }

    AdminPayoutScreenContent(
        payouts = state.payouts,
        isLoading = state.isLoading,
        isEndReached = state.isEndReached,
        onLoadMore = { viewModel.onEvent(PayoutEvent.LoadMorePayouts) },
        onAcceptClick = { id, status ->
            viewModel.onEvent(PayoutEvent.UpdatePayout(id, status))
        },
        onRejectClick = { id, status ->
            viewModel.onEvent(PayoutEvent.UpdatePayout(id, status))
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminPayoutScreenContent(
    payouts : List<Payout>,
    isLoading: Boolean = false,
    isEndReached: Boolean = false,
    onLoadMore: () -> Unit,
    onAcceptClick: (Int, String) -> Unit,
    onRejectClick: (Int, String) -> Unit,
) {
    val listState = rememberLazyListState()

    // Detect khi cuộn đến gần cuối
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount

                if (lastVisibleItem >= totalItems - 2 && !isLoading && !isEndReached) {
                    onLoadMore()
                }
            }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(MaterialTheme.dimens.medium1))
    } else if (payouts.isEmpty()) {
        androidx.compose.material3.Text(
            text = "Chưa có yêu cầu rút tiền nào",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.medium1)
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.small2)
        ) {
            items(payouts) { payout ->
                AdminPayoutCard(
                    payout = payout,
                    onApprove = { onAcceptClick(payout.id, PayoutStatus.SUCCESS.name) },
                    onReject = { onRejectClick(payout.id, PayoutStatus.FAILED.name) }
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.small1))
            }
            // loading indicator khi đang load thêm
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