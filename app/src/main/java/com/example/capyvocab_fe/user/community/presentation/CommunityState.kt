package com.example.capyvocab_fe.user.community.presentation

import com.example.capyvocab_fe.user.community.domain.model.Post

data class CommunityState (
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String = "",
    val currentPostPage: Int = 1,
    val isEndReachedPost: Boolean = false,
    val selectedPost: Post? = null,
)
