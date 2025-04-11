package com.example.capyvocab_fe.auth.data.mapper

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.auth.domain.model.UserRole
import com.example.capyvocab_fe.auth.domain.model.UserStatus
import com.example.capyvocab_fe.core.data.model.UserData

fun UserData.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        username = this.username,
        fullName = this.fullName,
        avatar = this.avatar ?: "",
       // role = UserRole.from(this.role)
    )
}