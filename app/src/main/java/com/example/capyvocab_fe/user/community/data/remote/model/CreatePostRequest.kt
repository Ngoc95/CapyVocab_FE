package com.example.capyvocab_fe.user.community.data.remote.model

data class CreatePostBody(
    val content: String?,
    val thumbnails: List<String>?,
    val tags: List<String>?,
)
