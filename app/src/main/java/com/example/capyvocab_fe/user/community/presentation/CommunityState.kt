package com.example.capyvocab_fe.user.community.presentation

import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post

data class CommunityState (
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val errorMessage: String = "",
    val currentPostPage: Int = 1,
    val isEndReachedPost: Boolean = false,
    val selectedPost: Post? = null,
    val selectedPostComment: List<Comment> = emptyList(),
    val childComment: Map<Int, List<Comment>> = mapOf(),

    val selectedComment: Comment? = null,
    val isCreateChildComment: Boolean = false,

    val selectUser: User? = null,
    val selectUserPosts: List<Post> = emptyList(),
)
