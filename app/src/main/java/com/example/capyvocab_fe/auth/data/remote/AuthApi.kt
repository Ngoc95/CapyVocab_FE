package com.example.capyvocab_fe.auth.data.remote

import com.example.capyvocab_fe.auth.data.remote.model.ChangePasswordRequest
import com.example.capyvocab_fe.auth.data.remote.model.GetAccountResponse
import com.example.capyvocab_fe.auth.data.remote.model.GoogleLoginRequest
import com.example.capyvocab_fe.auth.data.remote.model.LoginRequest
import com.example.capyvocab_fe.auth.data.remote.model.LoginResponse
import com.example.capyvocab_fe.auth.data.remote.model.RefreshResponse
import com.example.capyvocab_fe.auth.data.remote.model.RegisterRequest
import com.example.capyvocab_fe.auth.data.remote.model.RegisterResponse
import com.example.capyvocab_fe.core.network.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

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
    suspend fun verifyEmail(@Body codeRequest: Map<String, Int>): ApiResponse<Unit>
    @GET("auth/account")
    suspend fun getUserInfo(): ApiResponse<GetAccountResponse>
    @POST("oauth/google")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): LoginResponse

    @POST("emails/change-password")
    suspend fun sendChangePasswordEmail(@Body codeRequest: Map<String, String>): ApiResponse<Unit>

    @PUT("auth/change-password")
    suspend fun changePassword(
        @Query("code") code: String,
        @Body body: ChangePasswordRequest
    ): ApiResponse<Unit>

}
