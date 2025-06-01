package com.example.capyvocab_fe.user.community.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.user.community.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.repository.UserCommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val userCommunityRepository: UserCommunityRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CommunityState())
    val state: StateFlow<CommunityState> = _state

    fun onEvent(event: CommunityEvent) {
        when (event){
            is CommunityEvent.LoadPosts -> loadPosts()
            is CommunityEvent.GetPostById -> loadPostById(event.postId)
            is CommunityEvent.LoadMorePosts -> loadPosts(loadMore = true)
            is CommunityEvent.LoadComments -> loadComments(event.postId, event.parentCmtId)
            is CommunityEvent.VotePost -> votePost(event.post)
            is CommunityEvent.OnCreateChildCommentMode -> createChildCommentMode(event.comment)
            is CommunityEvent.OnCreateParentCommentMode -> createParentCommentMode()
            is CommunityEvent.CreateComment -> createComment(event.content)
            is CommunityEvent.ClearScreenPost -> clearScreenPost()
        }
    }

    private fun clearScreenPost() {
        Log.d("CommunityViewModel", "clearScreenPost called")
        _state.update {
            it.copy(
                selectedPost = null,
                selectedPostComment = emptyList(),
                selectedComment = null,
                childComment = emptyMap(),
            )
        }
    }

    private fun createComment(content: String) {
        viewModelScope.launch {
            val currentState = state.value
            val selectedPost = currentState.selectedPost
            val selectedComment = currentState.selectedComment

            if(state.value.isCreateChildComment && selectedPost != null && selectedComment != null)
            {
                val res = CreateCommentRequest(
                    content = content.trim(),
                    parentId = selectedComment.id
                )
                userCommunityRepository.createComment(res, selectedPost.id)
                    .onRight { comment ->
                        _state.update {
                            val currentChildComments = it.childComment[selectedComment.id] ?: emptyList()
                            val newChildCommentMap = it.childComment.toMutableMap()
                            newChildCommentMap[selectedComment.id] = currentChildComments + comment

                            it.copy(childComment = newChildCommentMap)
                        }
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Có lỗi xảy ra"
                            )
                        }
                    }
            }
            else if(selectedPost != null)
            {
                val res = CreateCommentRequest(
                    content = content,
                    parentId = null
                )
                userCommunityRepository.createComment(res, selectedPost.id)
                    .onRight { comment ->
                        _state.update {
                            val newListCmt = it.selectedPostComment + comment
                            val newSelectedPost = Post(
                                id = selectedPost.id,
                                content = selectedPost.content,
                                thumbnails = selectedPost.thumbnails,
                                tags = selectedPost.tags,
                                createdBy = selectedPost.createdBy,
                                createdAt = selectedPost.createdAt,
                                updatedAt = selectedPost.updatedAt,
                                voteCount = selectedPost.voteCount,
                                commentCount = selectedPost.commentCount + 1,
                                isAlreadyVote = selectedPost.isAlreadyVote
                            )
                            it.copy(
                                selectedPostComment = newListCmt,
                                selectedPost = newSelectedPost
                            )
                        }
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Có lỗi xảy ra"
                            )
                        }
                    }
                }
        }
    }

    private fun createParentCommentMode() {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isCreateChildComment = false,
                    selectedComment = null
                )
            }
        }
    }

    private fun createChildCommentMode(comment: Comment) {
        viewModelScope.launch {
            _state.update { currentState ->
                currentState.copy(
                    isCreateChildComment = true,
                    selectedComment = comment
                )
            }
        }
    }

    private fun loadPostById(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            userCommunityRepository.getPostById(id)
                .onRight { post ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedPost = post
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to load course"
                        )
                    }
                }
        }
    }

    private fun loadComments(postId: Int, parentCmtId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "")}
            userCommunityRepository.loadComment(postId, parentCmtId)
                .onRight { comments ->
                    _state.update { currentState ->
                        if (parentCmtId == null) {
                            val postComments = comments
                            currentState.copy(
                                selectedPostComment = comments,
                                childComment = mapOf()
                            )
                        } else {
                            val newChildCommentMap = currentState.childComment.toMutableMap()
                            newChildCommentMap[parentCmtId] = comments
                            currentState.copy(childComment = newChildCommentMap)
                        }
                    }
                }.onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Có lỗi xảy ra"
                        )
                    }
                }
        }

    }

    private fun votePost(post: Post) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            if(!post.isAlreadyVote) {
                userCommunityRepository.votePost(post.id)
                    .onRight {
                        _state.update { currentState ->
                            val updatedPosts = currentState.posts.map {
                                if (it.id == post.id) {
                                    val newIsAlreadyVote = !it.isAlreadyVote
                                    val newVoteCount =
                                        if (newIsAlreadyVote) it.voteCount + 1 else it.voteCount - 1
                                    it.copy(
                                        isAlreadyVote = newIsAlreadyVote,
                                        voteCount = newVoteCount
                                    )
                                } else it
                            }

                            val updatedSelectedPost = currentState.selectedPost?.let {
                                if (it.id == post.id) {
                                    val newIsAlreadyVote = !it.isAlreadyVote
                                    val newVoteCount =
                                        if (newIsAlreadyVote) it.voteCount + 1 else it.voteCount - 1
                                    it.copy(
                                        isAlreadyVote = newIsAlreadyVote,
                                        voteCount = newVoteCount
                                    )
                                } else it
                            }

                            currentState.copy(
                                posts = updatedPosts,
                                selectedPost = updatedSelectedPost
                            )
                        }
                    }
                    .onLeft {failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Có lỗi xảy ra"
                            )
                        }
                    }
            } else {
                userCommunityRepository.deleteVotePost(post.id)
                    .onRight {
                        _state.update { currentState ->
                            val updatedPosts = currentState.posts.map {
                                if (it.id == post.id) {
                                    val newIsAlreadyVote = !it.isAlreadyVote
                                    val newVoteCount =
                                        if (newIsAlreadyVote) it.voteCount + 1 else it.voteCount - 1
                                    it.copy(
                                        isAlreadyVote = newIsAlreadyVote,
                                        voteCount = newVoteCount
                                    )
                                } else it
                            }

                            val updatedSelectedPost = currentState.selectedPost?.let {
                                if (it.id == post.id) {
                                    val newIsAlreadyVote = !it.isAlreadyVote
                                    val newVoteCount =
                                        if (newIsAlreadyVote) it.voteCount + 1 else it.voteCount - 1
                                    it.copy(
                                        isAlreadyVote = newIsAlreadyVote,
                                        voteCount = newVoteCount
                                    )
                                } else it
                            }

                            currentState.copy(
                                posts = updatedPosts,
                                selectedPost = updatedSelectedPost
                            )
                        }
                    }
                    .onLeft {failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Có lỗi xảy ra"
                            )
                        }
                    }
            }
        }
    }




    private fun loadPosts(loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPostPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            userCommunityRepository.getAllPost(nextPage)
                .onRight { newPosts ->
                    _state.update {
                        val allPosts = if (loadMore) it.posts + newPosts else newPosts
                        it.copy(
                            isLoading = false,
                            posts = allPosts,
                            currentPostPage = nextPage,
                            isEndReachedPost = newPosts.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Đã xảy ra lỗi"
                        )
                    }
                }
        }
    }
}