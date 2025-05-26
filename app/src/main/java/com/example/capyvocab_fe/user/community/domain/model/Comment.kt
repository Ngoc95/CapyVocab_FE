package com.example.capyvocab_fe.user.community.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

data class Comment(
    val id: Int,
    val content: String,
    val parentComment: Comment?,
    val createdBy: User,
    val targetType: TargetType,
    val targetId: Int,
)
