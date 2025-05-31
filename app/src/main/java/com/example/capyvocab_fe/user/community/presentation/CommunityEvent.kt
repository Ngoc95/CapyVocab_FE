package com.example.capyvocab_fe.user.community.presentation

import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post

sealed class CommunityEvent {
    object LoadPosts : CommunityEvent()
    object LoadMorePosts : CommunityEvent()
    data class GetPostById(val postId: Int): CommunityEvent()
    data class VotePost(val post: Post): CommunityEvent()

    data class LoadCommentsByPost(val post: Post) : CommunityEvent()
    data class LoadChildComment(val comment: Comment) : CommunityEvent()
}