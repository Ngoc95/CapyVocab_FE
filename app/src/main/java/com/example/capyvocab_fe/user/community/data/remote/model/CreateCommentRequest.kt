package com.example.capyvocab_fe.user.community.data.remote.model

data class CreateCommentRequest (
    val content: String,
    val parentId: Int? = null
)
