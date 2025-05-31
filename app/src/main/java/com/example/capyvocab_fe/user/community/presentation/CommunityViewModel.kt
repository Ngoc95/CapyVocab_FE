package com.example.capyvocab_fe.user.community.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private fun loadPostById() {
        TODO("Not yet implemented")
    }

    fun onEvent(event: CommunityEvent) {
        when (event){
            is CommunityEvent.LoadPosts -> loadPosts()
            is CommunityEvent.GetPostById -> loadPostById()
            is CommunityEvent.LoadChildComment -> loadChildComment()
            is CommunityEvent.LoadMorePosts -> loadPosts(loadMore = true)
            is CommunityEvent.LoadCommentsByPost -> loadCommentByPosts()
            is CommunityEvent.VotePost -> votePost(event.post)
        }
    }

    private fun votePost(post: Post) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            if(post.isAlreadyVote) {
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
                            currentState.copy(posts = updatedPosts)
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
                            currentState.copy(posts = updatedPosts)
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

    private fun loadCommentByPosts() {
        TODO("Not yet implemented")
    }

    private fun loadChildComment() {
        TODO("Not yet implemented")
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