package com.example.capyvocab_fe.user.test.domain.model

import com.example.capyvocab_fe.auth.domain.model.User
import java.util.Date

data class Comment(
    val id: Int,
    val content: String = "",
    val createdBy: User,
    val parentComment: Comment? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date? = null,
    val childComments: List<Comment>? = null,
    val targetType: String? = null,
    val targetId: Int? = null
)
