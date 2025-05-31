package com.example.capyvocab_fe.user.community.data.remote

import com.example.capyvocab_fe.admin.user.data.remote.model.ImageUploadResponse
import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.community.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostRequest
import com.example.capyvocab_fe.user.community.data.remote.model.PostListResponse
import com.example.capyvocab_fe.user.community.data.remote.model.UpdateCommentRequest
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.model.Vote
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface UserCommunityApi {
    @GET("/posts")
    suspend fun getAllPost(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): ApiResponse<PostListResponse>

    @GET("/posts/{id}")
    suspend fun getPostById(
        @Path("id") id: Int,
    ): ApiResponse<Post>

    @POST("/posts")
    suspend fun createPost(@Body postRequest: CreatePostRequest): ApiResponse<List<Post>>

    @POST("posts/{id}/like")
    suspend fun votePost(
        @Path("id") id: Int,
    ): ApiResponse<Vote>

    @PATCH("/posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Body updatepostRequest: UpdatePostRequest
    ): ApiResponse<Post>

    @Multipart
    @POST("/upload/images")
    suspend fun uploadImage(
        @Part("type") type: RequestBody,
        @Part images: MultipartBody.Part
    ): ImageUploadResponse

//
    @POST("posts/{id}/comment")
    suspend fun createComment(
        @Path("id") postId: Int,
        @Body createComment: CreateCommentRequest,
    ): ApiResponse<Comment>

    @GET("posts/{id}/child-comment/{parentId}")
    suspend fun getChildComments(
        @Path("id") postId: Int,
        @Path("parentId") parentCommentId: Int
    ): ApiResponse<List<Comment>>

    @PATCH("posts/{id}/comment/{commentId}")
    suspend fun updateComment(
        @Path("id") postId: Int,
        @Path("commentId") commentId: Int,
        @Body body: UpdateCommentRequest,
    ): ApiResponse<Comment>

    @DELETE("posts/{id}/comment/{commentId}")
    suspend fun deleteComment(
        @Path("id") postId: Int,
        @Path("commentId") commentId: Int,
    ): ApiResponse<Unit>

    @DELETE("posts/{id}/unlike")
    suspend fun unVotePost(
        @Path("id") postId: Int,
    ): ApiResponse<Unit>
}