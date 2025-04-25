package com.example.capyvocab_fe.admin.user.data.remote.model

import com.example.capyvocab_fe.admin.user.data.model.UserData

data class UserListResponse(
    val metaData: MetaData
)

data class MetaData(
    val users: List<UserData>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)