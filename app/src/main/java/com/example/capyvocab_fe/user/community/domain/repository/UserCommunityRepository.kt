package com.example.capyvocab_fe.user.community.domain.repository

import android.net.Uri
import androidx.lifecycle.ViewModelStoreOwner
import arrow.core.Either
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.community.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostBody
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.model.Vote

interface UserCommunityRepository {
    suspend fun getAllPost(page: Int = 1, ownerId: Int? = null, tag: String? = null, content: String? = null): Either<AppFailure, List<Post>>
    suspend fun getPostById(id: Int): Either<AppFailure, Post>
    suspend fun createPost(postRequest: CreatePostBody): Either<AppFailure, Post>
    suspend fun updatePost(id: Int, postRequest: UpdatePostRequest): Either<AppFailure, Post>
    suspend fun uploadThumbnailImage(uri: List<Uri>): Either<AppFailure, List<String>>
    suspend fun votePost(id: Int): Either<AppFailure, Vote>
    suspend fun deleteVotePost(id: Int): Either<AppFailure, Unit>
    suspend fun uploadImage(uri: Uri): Either<AppFailure, String>
    suspend fun loadComment(postId: Int, parentCmtId: Int?): Either<AppFailure, List<Comment>>

    suspend fun createComment(createCommentRequest: CreateCommentRequest, postId: Int): Either<AppFailure, Comment>
    suspend fun getUserById(id: Int): Either<AppFailure, User>
}