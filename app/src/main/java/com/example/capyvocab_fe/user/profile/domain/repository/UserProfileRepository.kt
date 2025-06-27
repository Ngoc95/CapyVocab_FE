package com.example.capyvocab_fe.user.profile.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.profile.data.remote.model.DeleteUserResponse
import com.example.capyvocab_fe.user.profile.data.remote.model.LogoutResponse
import com.example.capyvocab_fe.user.profile.domain.model.ProfileUser

interface UserProfileRepository {
    suspend fun getPostByUser(page: Int = 1): Either<AppFailure, List<Post>>
    suspend fun updateUser(user: ProfileUser): Either<AppFailure, ProfileUser>

    suspend fun getUserInfor(id: Int): Either<AppFailure, ProfileUser>
    suspend fun uploadImage(uri: Uri): Either<AppFailure, String>

    suspend fun deleteUser(user: ProfileUser): Either<AppFailure, DeleteUserResponse>
    suspend fun logout(user: ProfileUser): Either<AppFailure, LogoutResponse>
}