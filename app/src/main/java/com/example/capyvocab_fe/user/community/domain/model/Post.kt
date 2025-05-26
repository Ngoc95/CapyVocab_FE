package com.example.capyvocab_fe.user.community.domain.model

import com.example.capyvocab_fe.auth.domain.model.User
import java.util.Date

data class Post (
    val id: Int,
    val content: String?,
    val thumbnails: List<String>?,
    val tags: List<String>?,
    val createdBy: User,
    val createdAt: Date,
    val updatedAt: Date?,
    val voteCount: Int,
    val commentCount: Int,
    val isAlreadyVote: Boolean,
)