package com.example.capyvocab_fe.user.community.presentation

import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post

sealed class CommunityEvent {
    object LoadPosts : CommunityEvent()
    object LoadMorePosts : CommunityEvent()
    data class GetPostById(val postId: Int): CommunityEvent()
    data class VotePost(val post: Post): CommunityEvent()

    data class OnCreateChildCommentMode(val comment: Comment): CommunityEvent()
    object OnCreateParentCommentMode: CommunityEvent()

    data class LoadComments(val postId: Int, val parentCmtId: Int?) : CommunityEvent()

    data class CreateComment(val content: String): CommunityEvent()
    object ClearScreenPost : CommunityEvent()
}