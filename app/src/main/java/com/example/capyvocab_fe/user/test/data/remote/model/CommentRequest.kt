package com.example.capyvocab_fe.user.test.data.remote.model

data class CreateCommentRequest(
    val content: String,
    val parentId: Int? = null
)

data class UpdateCommentRequest(
    val content: String
)