package com.example.capyvocab_fe.user.profile.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostRequest
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Post

interface UserProfileRepository {
    suspend fun getPostByUser(page: Int = 1): Either<AppFailure, List<Post>>
}
