package com.example.capyvocab_fe.user.notification.domain.model

import java.util.Date

/**
 * Enum representing notification types
 */
enum class NotificationType {
    CHANGE_PASSWORD,
    VOTE,
    COMMENT,
    ORDER,
    UNKNOWN
}

/**
 * Enum representing notification targets
 */
enum class NotificationTarget {
    ALL,
    ONLY_USER,
    SEGMENT,
    UNKNOWN
}

/**
 * Data class for notification data content
 */
data class NotificationData(
    val message: String,
    val data: Map<String, Any>?
)

/**
 * Domain model for a notification
 */
data class Notification(
    val id: Int,
    val type: NotificationType,
    val data: NotificationData,
    val target: NotificationTarget,
    val createdAt: Date?
)