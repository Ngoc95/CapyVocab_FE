package com.example.capyvocab_fe.admin.user.data.remote

import com.example.capyvocab_fe.admin.user.data.model.UserData
import com.example.capyvocab_fe.admin.user.data.remote.model.ApiResponse
import com.example.capyvocab_fe.admin.user.data.remote.model.CreateUserRequest
import com.example.capyvocab_fe.admin.user.data.remote.model.ImageUploadResponse
import com.example.capyvocab_fe.admin.user.data.remote.model.UpdateUserRequest
import com.example.capyvocab_fe.admin.user.data.remote.model.UserListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminUserApi {
    @GET("/users")
    suspend fun getAllUsers(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): UserListResponse

    @POST("/users")
    suspend fun createUser(
        @Body request: CreateUserRequest
    ): ApiResponse<UserData>


    @PATCH("/users/{id}")
    suspend fun updateUser(
        @Path("id") id: Int,
        @Body request: UpdateUserRequest
    ): ApiResponse<UserData>

    @Multipart
    @POST("/upload/images")
    suspend fun uploadAvatarImage(
        @Part("type") type: RequestBody,
        @Part images: MultipartBody.Part
    ): ImageUploadResponse

    @DELETE("/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: Int
    ): ApiResponse<Unit>
}
