package com.example.capyvocab_fe.user.notification.domain.model

import java.util.Date

data class UserNotification(
    val id: Int,
    val alreadyRead: Boolean,
    val readAt: Date?,
    val notification: Notification,
    val createdAt: Date?
)