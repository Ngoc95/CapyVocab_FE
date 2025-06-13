package com.example.capyvocab_fe.admin.dashboard.domain.model

import com.example.capyvocab_fe.auth.domain.model.User


data class FolderStats(
    val totalFolders: Int,
    val freeFolders: Int,
    val paidFolders: Int,
    val avgPaidFolderPrice: Double,
    val topFolders: List<TopFolder>
)

data class TopFolder(
    val id: Int,
    val name: String,
    val attemptCount: Int
)

data class TopFolderDetail(
    val id: Int,
    val name: String,
    val createdBy: User?,
    val price: Double,
    val voteCount: Int,
    val commentCount: Int,
    val questionCount: Int,
    val attemptCount: Int
)
