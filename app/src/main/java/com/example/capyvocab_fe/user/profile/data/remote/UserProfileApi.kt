package com.example.capyvocab_fe.user.profile.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.community.data.remote.model.PostListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserProfileApi {
    @GET("/posts")
    suspend fun getPostByUser(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("sort") sort: String? = "-createdAt",
    ): ApiResponse<PostListResponse>
}
