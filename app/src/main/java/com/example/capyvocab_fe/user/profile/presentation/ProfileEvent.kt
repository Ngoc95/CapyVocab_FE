package com.example.capyvocab_fe.user.profile.presentation

import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.learn.presentation.LearnEvent


sealed class ProfileEvent {

    //object LoadPosts : ProfileEvent()
    //object LoadMorePosts : ProfileEvent()
    object LoadCurrentUser : ProfileEvent()
    //data class GetPostById(val postId: Int): ProfileEvent()

    //data class LoadComments(val post: Post) : ProfileEvent()
    //data class LoadMoreComments(val post: Post ) : ProfileEvent()

    object ClearError : ProfileEvent()

}