package com.example.capyvocab_fe.user.notification.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.notification.data.dto.UserNotificationDto
import com.example.capyvocab_fe.user.notification.data.remote.model.NotificationListResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {
    @GET("notifications")
    suspend fun getUserNotifications(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): ApiResponse<NotificationListResponse>

    @PATCH("notifications/{id}/read")
    suspend fun markNotificationAsRead(@Path("id") notificationId: Int): ApiResponse<UserNotificationDto>
}