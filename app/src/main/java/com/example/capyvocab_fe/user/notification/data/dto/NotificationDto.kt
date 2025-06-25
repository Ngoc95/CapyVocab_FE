package com.example.capyvocab_fe.user.notification.data.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Data transfer object for notification types
 */
enum class NotificationTypeDto {
    @SerializedName("Change password")
    CHANGE_PASSWORD,

    @SerializedName("Vote")
    VOTE,

    @SerializedName("Comment")
    COMMENT,

    @SerializedName("Order")
    ORDER
}

/**
 * Data transfer object for notification targets
 */
enum class NotificationTargetDto {
    @SerializedName("All")
    ALL,

    @SerializedName("Only User")
    ONLY_USER,

    @SerializedName("Segment")
    SEGMENT
}

data class NotificationDataDto(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: Map<String, Any>?
)

data class NotificationDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("data")
    val data: NotificationDataDto,

    @SerializedName("target")
    val target: String,

    @SerializedName("createdAt")
    val createdAt: Date?
)