package com.example.capyvocab_fe.profile.data.remote.model

data class UpdatePasswordRequest(
    val oldPassword: String?,
    val newPassword: String?
)
