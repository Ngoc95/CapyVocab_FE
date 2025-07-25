package com.example.capyvocab_fe.user.community.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.data.TokenManager
import com.example.capyvocab_fe.user.community.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.community.data.remote.model.CreatePostBody
import com.example.capyvocab_fe.user.community.data.remote.model.UpdatePostRequest
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.repository.UserCommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val userCommunityRepository: UserCommunityRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _state = MutableStateFlow(CommunityState())
    val state: StateFlow<CommunityState> = _state

    init {
        viewModelScope.launch {
            val id = tokenManager.userId.first()
            _state.update { it.copy(currentUserId = id) }
        }
    }

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
            is CommunityEvent.CreatePost -> createPost(event.content, event.tags, event.images)
            is CommunityEvent.LoadPostsByOwner -> loadPostByOwner(event.user)
            is CommunityEvent.LoadMorePostsByOwner -> loadPostByOwner(event.user, loadMore = true)
            is CommunityEvent.ChangeToUserPost -> changeToUserPost()
            is CommunityEvent.GetUserByID -> getUserById(event.id)
            is CommunityEvent.UpdatePost -> updatePost(event.id, event.content, event.tags, event.images, event.existingThumbnailUrls)
            is CommunityEvent.LoadMyUser -> loadMyUser()
            is CommunityEvent.ResetPostCreated -> resetPostCreated()
            is CommunityEvent.ResetPostUpdated -> resetPostUpdated()
            is CommunityEvent.DeletePost -> deletePost(event.postId)
        }
    }

    private fun loadMyUser() {
        viewModelScope.launch {
            val id = tokenManager.userId.first();
            if(id != null)
                getUserById(id);
            else
            {
                _state.update {
                    it.copy(
                        errorMessage = "Có lỗi xảy ra"
                    )
                }
            }
        }
    }

    private fun updatePost(
        id: Int,
        content: String?,
        tags: List<String>?,
        thumbs: List<Uri>?,
        existingThumbnailUrls: List<String>? = null
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            
            // Lấy post hiện tại để có thumbnails cũ
            val currentPost = state.value.selectUserPosts.find { it.id == id }
            val existingThumbnails = currentPost?.thumbnails ?: emptyList()
            
            var thumbnailUrls: List<String>? = emptyList()
            
            // Kết hợp existing thumbnails với new thumbnails
            val finalThumbnails = mutableListOf<String>()
            
            // Thêm existing thumbnails (nếu có)
            existingThumbnailUrls?.let { finalThumbnails.addAll(it) }
            
            // Nếu có images mới được chọn, upload chúng và thêm vào
            if (!thumbs.isNullOrEmpty()) {
                val uploadResult = userCommunityRepository.uploadThumbnailImage(thumbs)
                thumbnailUrls = uploadResult.fold(
                    ifLeft = { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Có lỗi xảy ra"
                            )
                        }
                        return@launch
                    },
                    ifRight = { urls -> 
                        finalThumbnails.addAll(urls)
                        finalThumbnails
                    }
                )
            } else {
                thumbnailUrls = finalThumbnails
            }

            val request = UpdatePostRequest(
                content = content,
                thumbnails = thumbnailUrls,
                tags = tags
            )

            userCommunityRepository.updatePost(id, request)
                .onRight {post ->
                    _state.update { currentState ->
                        val updateUserPosts = currentState.selectUserPosts.map {
                            if (it.id == post.id) {
                                it.copy(
                                    content = post.content,
                                    tags = post.tags,
                                    thumbnails = post.thumbnails
                                )
                            } else it
                        }
                        
                        // Cập nhật selectedPost nếu đang edit post này
                        val updatedSelectedPost = currentState.selectedPost?.let {
                            if (it.id == post.id) {
                                it.copy(
                                    content = post.content,
                                    tags = post.tags,
                                    thumbnails = post.thumbnails
                                )
                            } else it
                        }
                        
                        currentState.copy(
                            isLoading = false,
                            selectUserPosts = updateUserPosts,
                            selectedPost = updatedSelectedPost,
                            isPostUpdated = true
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

    private fun getUserById(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            userCommunityRepository.getUserById(id)
                .onRight { user ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectUser = user
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

    private fun changeToUserPost() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentPostPage = 1,
                    isEndReachedPost= false,
                    selectUserPosts = emptyList(),
                    selectUser = null,
                )
            }
        }
    }

    private fun loadPostByOwner(user: User, loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPostPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            userCommunityRepository.getAllPost(page = nextPage, ownerId = user.id)
                .onRight { newPosts ->
                    _state.update {
                        val allPosts = if (loadMore) it.selectUserPosts + newPosts else newPosts
                        it.copy(
                            isLoading = false,
                            selectUserPosts = allPosts,
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


    private fun createPost(content: String?, tags: List<String>?, images: List<Uri>?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            var thumbnailUrls: List<String>? = emptyList()
            if(!images.isNullOrEmpty()) {
                val uploadResult = userCommunityRepository.uploadThumbnailImage(images)
                 thumbnailUrls = uploadResult.fold(
                    ifLeft = { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Có lỗi xảy ra"
                            )
                        }
                        emptyList()
                    },
                    ifRight = { urls -> urls }
                )
            }
            else
                thumbnailUrls = emptyList()

            val request = CreatePostBody(
                content = content,
                thumbnails = thumbnailUrls,
                tags = tags
            )

            userCommunityRepository.createPost(request)
                .onRight { post ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            isPostCreated = true
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

    private fun clearScreenPost() {
        _state.update {
            it.copy(
                selectedPost = null,
                selectedPostComment = emptyList(),
                selectedComment = null,
                childComment = emptyMap(),
                isPostCreated = false,
                isPostUpdated = false,
            )
        }
    }

    private fun resetPostCreated() {
        _state.update {
            it.copy(isPostCreated = false)
        }
    }

    private fun resetPostUpdated() {
        _state.update {
            it.copy(isPostUpdated = false)
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

                            val updateUserPosts = currentState.selectUserPosts.map {
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
                                selectedPost = updatedSelectedPost,
                                selectUserPosts = updateUserPosts,
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

                            val updateUserPosts = currentState.selectUserPosts.map {
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
                                selectedPost = updatedSelectedPost,
                                selectUserPosts = updateUserPosts
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
            userCommunityRepository.getAllPost(page = nextPage)
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

    private fun deletePost(postId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            val result = userCommunityRepository.deletePost(postId)
            result.fold(
                ifLeft = { failure ->
                    _state.update { it.copy(isLoading = false, errorMessage = failure.message ?: "Xoá bài viết thất bại") }
                },
                ifRight = {
                    _state.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            posts = currentState.posts.filter { it.id != postId },
                            selectUserPosts = currentState.selectUserPosts.filter { it.id != postId },
                            errorMessage = ""
                        )
                    }
                }
            )
        }
    }
}