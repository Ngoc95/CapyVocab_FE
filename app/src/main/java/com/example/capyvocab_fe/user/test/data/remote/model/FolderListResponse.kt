package com.example.capyvocab_fe.user.test.data.remote.model

import com.example.capyvocab_fe.user.test.domain.model.Folder

data class FolderListResponse(
    val folders: List<Folder>,
    val total: Int,
    val currentPage: Int,
    val totalPages: Int
)
