package com.example.capyvocab_fe.user.notification.data.mapper

import com.example.capyvocab_fe.user.notification.data.dto.NotificationDto
import com.example.capyvocab_fe.user.notification.data.dto.UserNotificationDto
import com.example.capyvocab_fe.user.notification.domain.model.Notification
import com.example.capyvocab_fe.user.notification.domain.model.NotificationData
import com.example.capyvocab_fe.user.notification.domain.model.NotificationTarget
import com.example.capyvocab_fe.user.notification.domain.model.NotificationType
import com.example.capyvocab_fe.user.notification.domain.model.UserNotification
import javax.inject.Inject

class NotificationMapper @Inject constructor() {
    fun mapToDomain(dto: NotificationDto): Notification {
        return Notification(
            id = dto.id,
            type = mapNotificationType(dto.type),
            data = NotificationData(
                message = dto.data.message,
                data = dto.data.data
            ),
            target = mapNotificationTarget(dto.target),
            createdAt = dto.createdAt
        )
    }
    fun mapToDomain(dto: UserNotificationDto): UserNotification {
        return UserNotification(
            id = dto.id,
            alreadyRead = dto.alreadyRead,
            readAt = dto.readAt,
            notification = mapToDomain(dto.notification),
            createdAt = dto.createdAt
        )
    }
    fun mapToDomain(dtos: List<UserNotificationDto>): List<UserNotification> {
        return dtos.map { mapToDomain(it) }
    }
    private fun mapNotificationType(type: String): NotificationType {
        return when (type) {
            "Change password" -> NotificationType.CHANGE_PASSWORD
            "Vote" -> NotificationType.VOTE
            "Comment" -> NotificationType.COMMENT
            "Order" -> NotificationType.ORDER
            else -> NotificationType.UNKNOWN
        }
    }
    private fun mapNotificationTarget(target: String): NotificationTarget {
        return when (target) {
            "All" -> NotificationTarget.ALL
            "Only User" -> NotificationTarget.ONLY_USER
            "Segment" -> NotificationTarget.SEGMENT
            else -> NotificationTarget.UNKNOWN
        }
    }
    fun mapToUserNotification(notification: Notification): UserNotification {
        return UserNotification(
            id = notification.id,
            alreadyRead = false,
            readAt = null,
            notification = notification,
            createdAt = notification.createdAt
        )
    }
}