package com.example.capyvocab_fe.user.test.data.remote

import com.example.capyvocab_fe.core.network.ApiResponse
import com.example.capyvocab_fe.user.test.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.data.remote.model.FolderListResponse
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateCommentRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Comment
import com.example.capyvocab_fe.user.test.domain.model.Folder
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseApi {
    @GET("exercise")
    suspend fun getAllFolders(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
        @Query("name") name: String? = null,
        @Query("code") code: String? = null
    ): ApiResponse<FolderListResponse>

    @GET("exercise/{id}")
    suspend fun getFolderById(@Path("id") id: Int): ApiResponse<Folder>

    @POST("exercise/new-folder")
    suspend fun createFolder(@Body body: CreateFolderRequest): ApiResponse<Folder>

    @PATCH("exercise/{id}")
    suspend fun updateFolder(
        @Path("id") id: Int,
        @Body body: UpdateFolderRequest
    ): ApiResponse<Folder>

    @DELETE("exercise/{id}")
    suspend fun deleteFolder(@Path("id") id: Int): ApiResponse<Any>

    @POST("exercise/{id}/like")
    suspend fun voteFolder(@Path("id") id: Int): ApiResponse<Any>

    @POST("exercise/{id}/unlike")
    suspend fun unVoteFolder(@Path("id") id: Int): ApiResponse<Any>

    @GET("exercise/{id}/comments")
    suspend fun getFolderComments(@Path("id") id: Int): ApiResponse<List<Comment>>

    @POST("exercise/{id}/comment")
    suspend fun createComment(
        @Path("id") id: Int,
        @Body body: CreateCommentRequest
    ): ApiResponse<Comment>

    @GET("exercise/{id}/child-comment/{parentID}")
    suspend fun getChildComments(
        @Path("id") id: Int,
        @Path("parentID") parentID: Int
    ): ApiResponse<List<Comment>>

    @PUT("exercise/{id}/comment/{commentID}")
    suspend fun updateComment(
        @Path("id") id: Int,
        @Path("commentID") commentID: Int,
        @Body body: UpdateCommentRequest
    ): ApiResponse<Comment>

    @DELETE("exercise/{id}/comment/{commentID}")
    suspend fun deleteComment(
        @Path("id") id: Int,
        @Path("commentID") commentID: Int
    ): ApiResponse<Any>
}