package com.example.capyvocab_fe.admin.user.data.mapper

import com.example.capyvocab_fe.admin.user.data.model.UserData
import com.example.capyvocab_fe.admin.user.domain.model.User

fun UserData.toDomain(): User = User(
    id = id,
    email = email,
    username = username,
    fullName = fullName,
    avatar = avatar,
    status = status
)

fun User.toUserData(): UserData = UserData(
    id = id,
    email = email,
    username = username,
    fullName = fullName,
    avatar = avatar,
    status = status
)