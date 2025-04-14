package com.example.capyvocab_fe.core.data.model

import com.google.gson.annotations.SerializedName

data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val fullName: String,
    val avatar: String?,

    //val role: RoleData
)


data class RoleData(
    val id: Int,
    val name: String,
    val description: String?,
    val isDeleted: Boolean
)