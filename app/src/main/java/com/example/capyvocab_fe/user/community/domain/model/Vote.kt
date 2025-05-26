package com.example.capyvocab_fe.user.community.domain.model

import com.example.capyvocab_fe.auth.domain.model.User

data class Vote(
    val id: Int,
    val createBy: User,
    val targetType: TargetType,
    val targetId: Int,
)
