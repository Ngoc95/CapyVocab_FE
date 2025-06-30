//package com.example.capyvocab_fe.user.profile.data.remote
//
//import com.example.capyvocab_fe.admin.user.data.remote.model.ImageUploadResponse
//import com.example.capyvocab_fe.core.network.ApiResponse
//import com.example.capyvocab_fe.user.community.data.remote.model.PostListResponse
//import com.example.capyvocab_fe.user.profile.data.remote.model.DeleteUserResponse
//import com.example.capyvocab_fe.user.profile.data.remote.model.LogoutResponse
//import com.example.capyvocab_fe.user.profile.data.remote.model.UpdateUserRequest
//import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//import retrofit2.http.Body
//import retrofit2.http.DELETE
//import retrofit2.http.GET
//import retrofit2.http.Multipart
//import retrofit2.http.PATCH
//import retrofit2.http.POST
//import retrofit2.http.Part
//import retrofit2.http.Path
//import retrofit2.http.Query
//
//interface UserProfileApi {
//    @GET("/posts")
//    suspend fun getPostByUser(
//        @Query("page") page: Int = 1,
//        @Query("limit") limit: Int = 10,
//        @Query("sort") sort: String? = "-createdAt",
//    ): ApiResponse<PostListResponse>
//
//    @PATCH("/users/{id}")
//    suspend fun updateUser(
//        @Path("id") id: Int,
//        @Body updateRequest: UpdateUserRequest
//    ): ApiResponse<ProfileUser>
//
//    @GET("/users/{id}")
//    suspend fun getUserInfor(
//        @Path("id") id: Int
//    ): ApiResponse<ProfileUser>
//
//    @Multipart
//    @POST("/upload/images")
//    suspend fun uploadImage(
//        @Part("type") type: RequestBody,
//        @Part images: MultipartBody.Part
//    ): ImageUploadResponse
//
//    @POST("/logout")
//    suspend fun logout(
//    ): ApiResponse<LogoutResponse>
//
//    @DELETE("users/{id}")
//    suspend fun deleteUserById(
//        @Path("id") userId: Int
//    ): ApiResponse<DeleteUserResponse>
//}
