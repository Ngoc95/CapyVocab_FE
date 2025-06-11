package com.example.capyvocab_fe.user.community.data.remote.model

import com.example.capyvocab_fe.auth.domain.model.User

data class UpdatePostRequest (
    val content: String?,
    val thumbnails: List<String>?,
    val tags: List<String>?,
)