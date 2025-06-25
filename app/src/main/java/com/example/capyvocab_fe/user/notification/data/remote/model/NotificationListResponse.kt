package com.example.capyvocab_fe.user.notification.data.remote.model

import com.example.capyvocab_fe.user.notification.data.dto.UserNotificationDto

data class NotificationListResponse(
    val notifications: List<UserNotificationDto>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)
