package com.example.capyvocab_fe.profile.data.remote

import com.example.capyvocab_fe.admin.user.data.model.UserData
import com.example.capyvocab_fe.auth.data.remote.model.LogoutRequest
import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.profile.data.remote.model.GetProfileResponse
import com.example.capyvocab_fe.profile.data.remote.model.UpdatePasswordRequest
import com.example.capyvocab_fe.profile.data.remote.model.UpdateProfileRequest
import com.example.capyvocab_fe.profile.domain.model.UserProfile
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileApi {
    @GET("auth/account")
    suspend fun getProfile(): ApiResponse<GetProfileResponse>
    @POST("auth/logout")
    suspend fun logout(@Body body: LogoutRequest): ApiResponse<Unit>
    @PATCH("/users/{id}")
    suspend fun updateProfile(
        @Path("id") id: Int,
        @Body request: UpdateProfileRequest
    ): ApiResponse<UserProfile>

    @PATCH("/users/{id}")
    suspend fun updatePassword(
        @Path("id") id: Int,
        @Body request: UpdatePasswordRequest
    ): ApiResponse<UserData>
}