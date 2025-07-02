package com.example.capyvocab_fe.user.community.data.repository

import android.net.Uri
import arrow.core.Either
import arrow.core.Either.Companion.catch
import com.example.capyvocab_fe.MyApplication
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.community.data.remote.UserCommunityApi
import com.example.capyvocab_fe.user.community.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostBody
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.model.Vote
import com.example.capyvocab_fe.user.community.domain.repository.UserCommunityRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject

class UserCommunityRepositoryImpl @Inject constructor(
    private val api: UserCommunityApi
) : UserCommunityRepository {

    override suspend fun getAllPost(page: Int, ownerId: Int?, tag: String?, content: String?): Either<AppFailure, List<Post>> {
        return catch {
            api.getAllPost(page = page, ownerId = ownerId, tag = tag, content = content).metaData.posts
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun createPost(postRequest: CreatePostBody): Either<AppFailure, Post> {
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

    override suspend fun updatePost(
        id: Int,
        postRequest: UpdatePostRequest
    ): Either<AppFailure, Post> {
        return catch {
            api.updatePost(id, postRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun uploadThumbnailImage(uriList: List<Uri>): Either<AppFailure, List<String>> {
        return Either.catch {
            val contentResolver = MyApplication.instance.contentResolver

            coroutineScope {
                uriList.map { uri ->
                    async {
                        val inputStream = contentResolver.openInputStream(uri)
                            ?: throw IOException("Không mở được ảnh: $uri")

                        val fileName = "post_image.jpg"
                        val requestBody = inputStream.readBytes()
                            .toRequestBody("image/*".toMediaTypeOrNull())

                        val multipart = MultipartBody.Part.createFormData("AVATAR", fileName, requestBody)
                        val typePart = "AVATAR".toRequestBody("text/plain".toMediaType())

                        val response = api.uploadImage(typePart, multipart)
                        response.metaData.firstOrNull()?.destination
                            ?: throw IOException("Không nhận được URL ảnh từ: $uri")
                    }
                }.awaitAll()
            }
        }.mapLeft { it.toAppFailure() }
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

    override suspend fun loadComment(
        postId: Int,
        parentCmtId: Int?
    ): Either<AppFailure, List<Comment>> {
        return catch {
            api.getChildComments(postId, parentCmtId.toString()).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun createComment(createCommentRequest: CreateCommentRequest, postId: Int): Either<AppFailure, Comment> {
        return catch {
            api.createComment(postId, createCommentRequest).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun getUserById(id: Int): Either<AppFailure, User> {
        return catch {
            api.getUserById(id).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

    override suspend fun deletePost(id: Int): Either<AppFailure, Unit> {
        return catch {
            api.deletePost(id).metaData
        }.mapLeft {
            it.toAppFailure()
        }
    }

}