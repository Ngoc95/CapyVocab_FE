package com.example.capyvocab_fe.user.notification.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class UserNotificationDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("alreadyRead")
    val alreadyRead: Boolean,

    @SerializedName("readAt")
    val readAt: Date?,

    @SerializedName("notification")
    val notification: NotificationDto,

    @SerializedName("createdAt")
    val createdAt: Date?
)