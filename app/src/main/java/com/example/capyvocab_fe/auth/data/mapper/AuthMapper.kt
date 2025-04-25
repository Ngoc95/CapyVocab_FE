package com.example.capyvocab_fe.auth.data.mapper

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.auth.data.remote.model.UserData

fun UserData.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        username = this.username,
        avatar = this.avatar ?: "",
        roleId = role.id
    )
}