package com.example.capyvocab_fe.auth.data.remote

import com.example.capyvocab_fe.auth.data.remote.model.LoginRequest
import com.example.capyvocab_fe.auth.data.remote.model.LoginResponse
import com.example.capyvocab_fe.auth.data.remote.model.RegisterRequest
import com.example.capyvocab_fe.auth.data.remote.model.RegisterResponse
import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

}

