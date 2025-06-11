package com.example.capyvocab_fe.user.test.data.remote.model

data class CreateFolderRequest(
    val name: String,
    val price: Double,
    val isPublic: Boolean? = null
)
