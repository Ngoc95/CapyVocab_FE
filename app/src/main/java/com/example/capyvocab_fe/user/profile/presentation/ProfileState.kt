package com.example.capyvocab_fe.user.profile.presentation

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.community.domain.model.Post

data class ProfileState (
    val isLoading: Boolean = false,
    val courses: List<Post> = emptyList(),
    val errorMessage: String = "",
    val currentPostPage: Int = 1,
    val isEndReachedCourse: Boolean = false,
    val selectedPost: Post? = null,
    val currentUser: User? = null,
)