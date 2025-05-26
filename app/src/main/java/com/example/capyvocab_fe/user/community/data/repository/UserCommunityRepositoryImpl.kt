package com.example.capyvocab_fe.user.community.data.repository

import android.net.Uri
import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.community.data.remote.UserCommunityApi
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostRequest
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.repository.UserCommunityRepository
import javax.inject.Inject

class UserCommunityRepositoryImpl @Inject constructor(
    private val api: UserCommunityApi
) : UserCommunityRepository {

    override suspend fun getAllPost(page: Int): Either<AppFailure, List<Post>> {
        return Either.catch {
            api.getAllPost(page).metaData.posts
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun createPost(postRequest: CreatePostRequest): Either<AppFailure, List<Post>> {
        return Either.catch {
            api.createPost(postRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun getPostById(id: Int): Either<AppFailure, Post> {
        return Either.catch {
            api.getPostById(id).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun updateTopic(
        id: Int,
        postRequest: UpdatePostRequest
    ): Either<AppFailure, Post> {
        return catch {
            api.updatePost(id, postRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun uploadThumbnailImage(uri: Uri): Either<AppFailure, String> {
        TODO("Not yet implemented")
    }
}