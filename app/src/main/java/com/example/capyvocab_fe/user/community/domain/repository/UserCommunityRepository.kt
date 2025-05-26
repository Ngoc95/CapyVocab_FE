package com.example.capyvocab_fe.user.community.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostRequest
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Post

interface UserCommunityRepository {
    suspend fun getAllPost(page: Int = 1): Either<AppFailure, List<Post>>
    suspend fun getPostById(id: Int): Either<AppFailure, Post>
    suspend fun createPost(postRequest: CreatePostRequest): Either<AppFailure, List<Post>>
    suspend fun updateTopic(id: Int, postRequest: UpdatePostRequest): Either<AppFailure, Post>
    suspend fun uploadThumbnailImage(uri: Uri): Either<AppFailure, String>
}