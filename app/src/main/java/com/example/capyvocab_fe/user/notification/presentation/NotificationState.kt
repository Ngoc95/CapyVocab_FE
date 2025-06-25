package com.example.capyvocab_fe.user.notification.presentation

import com.example.capyvocab_fe.user.notification.domain.model.UserNotification

data class NotificationState(
    val notifications: List<UserNotification> = emptyList(),
    val currentPage: Int = 1,
    val isEndReached: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val unreadCount: Int = 0,
)
