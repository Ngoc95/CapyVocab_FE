package com.example.capyvocab_fe.user.community.data.remote.model

import com.example.capyvocab_fe.user.community.domain.model.Post

data class PostListResponse (
    val posts: List<Post>,
    val total: Int = 0,
    val currentPage: Int = 1,
    val totalPages: Int = 0
)