package com.example.capyvocab_fe.user.profile.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.profile.data.remote.UserProfileApi
import com.example.capyvocab_fe.user.profile.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val api: UserProfileApi
) : UserProfileRepository {
    override suspend fun getPostByUser(page: Int): Either<AppFailure, List<Post>> {
        return Either.catch {
            val response = api.getPostByUser(page = 1)
            response.metaData.posts
        }.mapLeft { it.toAppFailure() }
    }
}