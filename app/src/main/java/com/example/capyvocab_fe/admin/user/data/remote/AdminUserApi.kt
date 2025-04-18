package com.example.capyvocab_fe.admin.user.data.remote

import com.example.capyvocab_fe.admin.user.data.model.UserData
import com.example.capyvocab_fe.admin.user.data.remote.model.UserListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminUserApi {
    @GET("/users")
    suspend fun getAllUsers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): UserListResponse

    @POST("/users")
    suspend fun createUser(@Body user: UserData): UserData

    @PUT("/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserData): UserData
}
