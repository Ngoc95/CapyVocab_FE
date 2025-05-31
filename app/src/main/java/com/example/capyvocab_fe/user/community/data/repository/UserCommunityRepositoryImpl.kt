package com.example.capyvocab_fe.user.community.data.repository

import android.net.Uri
import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.community.data.remote.UserCommunityApi
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostRequest
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.model.Vote
import com.example.capyvocab_fe.user.community.domain.repository.UserCommunityRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class UserCommunityRepositoryImpl @Inject constructor(
    private val api: UserCommunityApi
) : UserCommunityRepository {

    override suspend fun getAllPost(page: Int): Either<AppFailure, List<Post>> {
        return catch {
            api.getAllPost(page).metaData.posts
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun createPost(postRequest: CreatePostRequest): Either<AppFailure, List<Post>> {
        return catch {
            api.createPost(postRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun getPostById(id: Int): Either<AppFailure, Post> {
        return catch {
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

    override suspend fun votePost(id: Int): Either<AppFailure, Vote> {
        return catch{
            api.votePost(id).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun deleteVotePost(id: Int): Either<AppFailure, Unit> {
        return catch{
            api.unVotePost(id).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun uploadImage(uri: Uri): Either<AppFailure, String> {
        return catch {
            val contentResolver = MyApplication.instance.contentResolver
            val inputStream =
                contentResolver.openInputStream(uri) ?: throw IOException("Không mở được ảnh")
            val fileName = "${System.currentTimeMillis()}.jpg"
            val requestBody = inputStream.readBytes().toRequestBody("image/*".toMediaTypeOrNull())

            val multipart = MultipartBody.Part.createFormData("AVATAR", fileName, requestBody)
            val typePart = "AVATAR".toRequestBody("text/plain".toMediaType())

            val response = api.uploadImage(typePart, multipart)
            response.metaData.firstOrNull()?.destination
                ?: throw IOException("Không nhận được URL ảnh")
        }.mapLeft { it.toAppFailure() }
    }
}