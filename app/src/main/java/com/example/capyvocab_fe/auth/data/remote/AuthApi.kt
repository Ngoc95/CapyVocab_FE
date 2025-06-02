package com.example.capyvocab_fe.auth.data.remote

import com.example.capyvocab_fe.auth.data.remote.model.LoginRequest
import com.example.capyvocab_fe.auth.data.remote.model.LoginResponse
import com.example.capyvocab_fe.auth.data.remote.model.RefreshResponse
import com.example.capyvocab_fe.auth.data.remote.model.RegisterRequest
import com.example.capyvocab_fe.auth.data.remote.model.RegisterResponse
import com.example.capyvocab_fe.core.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: Map<String, String>): RefreshResponse

    @POST("emails/send-verification")
    suspend fun sendVerificationEmail(): ApiResponse<Unit>

    @POST("auth/verify-email")
    suspend fun verifyEmail(@Body code: Int): ApiResponse<Unit>
}

