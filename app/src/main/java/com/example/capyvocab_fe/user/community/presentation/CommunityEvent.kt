package com.example.capyvocab_fe.user.community.presentation

import android.net.Uri
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post

sealed class CommunityEvent {
    object LoadPosts : CommunityEvent()
    object LoadMorePosts : CommunityEvent()
    data class GetPostById(val postId: Int) : CommunityEvent()
    data class VotePost(val post: Post) : CommunityEvent()

    data class OnCreateChildCommentMode(val comment: Comment) : CommunityEvent()
    object OnCreateParentCommentMode : CommunityEvent()

    data class LoadComments(val postId: Int, val parentCmtId: Int?) : CommunityEvent()

    data class CreateComment(val content: String) : CommunityEvent()
    object ClearScreenPost : CommunityEvent()
    data class CreatePost(val content: String?, val tags: List<String>?, val images: List<Uri>?) : CommunityEvent()

    data class LoadPostsByOwner(val user: User) : CommunityEvent()
    data class LoadMorePostsByOwner(val user: User) : CommunityEvent()
    object ChangeToUserPost : CommunityEvent()
    data class GetUserByID(val id: Int) : CommunityEvent()
    data class UpdatePost(val id: Int, val content: String?, val tags: List<String>?, val images: List<Uri>?) : CommunityEvent()
    object LoadMyUser : CommunityEvent()
}