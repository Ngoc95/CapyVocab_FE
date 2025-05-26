package com.example.capyvocab_fe.user.community.data.remote.model

import com.example.capyvocab_fe.auth.domain.model.User
import java.util.Date

data class CreatePostRequest(
    val topics: List<CreatePostBody>
)

data class CreatePostBody(
    val content: String,
    val thumbnails: List<String>?,
    val user: User,
)
