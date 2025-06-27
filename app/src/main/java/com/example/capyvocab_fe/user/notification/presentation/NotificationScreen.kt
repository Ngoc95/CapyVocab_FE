package com.example.capyvocab_fe.user.notification.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import com.example.capyvocab_fe.user.notification.presentation.components.NotificationItem
import com.example.capyvocab_fe.user.notification.presentation.handler.NotificationActionHandler

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    notificationActionHandler: NotificationActionHandler,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.onEvent(NotificationEvent.LoadNotifications)
    }

    NotificationScreenContent(
        notifications = state.notifications.sortedByDescending{ it.createdAt },
        isLoading = state.isLoading,
        isEndReached = state.isEndReached,
        error = state.error,
        onBack = { navController.popBackStack() },
        onLoad = { viewModel.onEvent(NotificationEvent.LoadNotifications) },
        onLoadMore = { viewModel.onEvent(NotificationEvent.LoadMoreNotifications) },
        onNotificationClick = { notification ->
            viewModel.onEvent(NotificationEvent.MarkAsRead(notification.id))
            notificationActionHandler.handleNotificationClick(notification)
        }
    )
}

@Composable
fun NotificationScreenContent(
    notifications: List<UserNotification>,
    isLoading: Boolean,
    isEndReached: Boolean,
    error: String?,
    onBack: () -> Unit,
    onLoad: () -> Unit,
    onLoadMore: () -> Unit,
    onNotificationClick: (UserNotification) -> Unit,
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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top app bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.backicon),
                    modifier = Modifier.size(50.dp)
                        .clickable { onBack() },
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Thông báo",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column {
                        Text(text = error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onLoad() }) {
                            Text("Thử lại")
                        }
                    }
                }
            } else if (notifications.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Không có thông báo nào")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(notifications) { index, notification ->
                        NotificationItem(
                            notification = notification,
                            onNotificationClick = { onNotificationClick(notification) },
                        )
                        if (index == notifications.size - 1 && !isLoading && !isEndReached) {
                            onLoadMore()
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
    }
}