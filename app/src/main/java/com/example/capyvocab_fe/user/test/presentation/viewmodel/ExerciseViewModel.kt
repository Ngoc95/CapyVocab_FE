package com.example.capyvocab_fe.user.test.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.auth.domain.repository.AuthRepository
import com.example.capyvocab_fe.core.error.AppError
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.data.remote.model.QuestionRequest
import com.example.capyvocab_fe.user.test.data.remote.model.QuizRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.domain.model.Question
import com.example.capyvocab_fe.user.test.domain.model.Quiz
import com.example.capyvocab_fe.user.test.domain.repository.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ExerciseState())
    val state: StateFlow<ExerciseState> = _state
    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        viewModelScope.launch {
            authRepository.getUserInfo().fold(
                {
                    _state.update { it.copy(error = it.error) }
                },
                { user ->
                    _state.update { it.copy(currentUser = user) }
                }
            )
        }
    }

    /**
     * Processes user events and updates the state accordingly.
     * @param event The user event to process
     */
    fun onEvent(event: ExerciseEvent) {
        when (event) {
            is ExerciseEvent.GetAllFolders -> getAllFolders(event.page, event.limit, event.name, event.code)
            is ExerciseEvent.GetFolderById -> getFolderById(event.id)
            is ExerciseEvent.CreateFolder -> createFolder(event.request, event.onSuccess, event.onError)
            is ExerciseEvent.UpdateFolder -> updateFolder(event.id, event.request)
            is ExerciseEvent.DeleteFolder -> deleteFolder(event.id)
            is ExerciseEvent.VoteFolder -> voteFolder(event.id)
            is ExerciseEvent.UnvoteFolder -> unvoteFolder(event.id)
            is ExerciseEvent.CreateComment -> createComment(
                event.folderId,
                event.content,
                event.parentId
            )

            is ExerciseEvent.GetChildComments -> getChildComments(event.folderId, event.parentId)
            is ExerciseEvent.UpdateComment -> updateComment(
                event.folderId,
                event.commentId,
                event.content
            )
            is ExerciseEvent.DeleteComment -> deleteComment(event.folderId, event.commentId)

            is ExerciseEvent.UpdateQuiz -> updateQuiz(event.quizId, event.title, event.questions)
            is ExerciseEvent.AddQuestionToQuiz -> addQuestionToQuiz(event.quizId, event.question)
            is ExerciseEvent.UpdateQuestionInQuiz -> updateQuestionInQuiz(event.quizId, event.questionIndex, event.question)
            is ExerciseEvent.DeleteQuestionFromQuiz -> deleteQuestionFromQuiz(event.quizId, event.questionIndex)
            is ExerciseEvent.SaveFolderWithQuizzes -> saveFolderWithQuizzes(event.folderId)
            // Navigation events
            is ExerciseEvent.NavigateToDoTest -> _state.update { it.copy(currentTab = 0) }
            is ExerciseEvent.NavigateToEnterCode -> _state.update { it.copy(currentTab = 1) }
            is ExerciseEvent.NavigateToCreatedTests -> _state.update { it.copy(currentTab = 2) }
            is ExerciseEvent.NavigateToCreateTest -> _state.update { it.copy(currentTab = 3) }

            // Reset events
            is ExerciseEvent.ResetError -> _state.update { it.copy(error = null) }
            is ExerciseEvent.ResetSuccess -> _state.update { it.copy(successMessage = null) }
        }
    }
    /**
     * Fetches all folders with pagination.
     */
    private fun getAllFolders(page: Int, limit: Int, name: String? = null, code: String? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            exerciseRepository.getAllFolders(page, limit, name, code).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { folders ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            folders = if (page == 1) folders else it.folders + folders,
                            currentPage = page,
                            hasMorePages = folders.isNotEmpty() && folders.size == limit
                        )
                    }
                }
            )
        }
    }

    /**
     * Fetches a folder by its ID.
     */
    private fun getFolderById(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            exerciseRepository.getFolderById(id).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { folder ->
                    _state.update { it.copy(
                        isLoading = false,
                        currentFolder = folder,
                        currentQuiz = folder.quizzes?.firstOrNull(),
                        comments = folder.comments ?: emptyList()
                    ) }
                    // Gọi hàm đảm bảo có quiz nếu chưa có
                    ensureQuizExists()
                }
            )
        }
    }

    /**
     * Creates a new folder.
     */
    private fun createFolder(
        request: CreateFolderRequest,
        onSuccess: (Folder) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            exerciseRepository.createFolder(request).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                    onError(failure.message ?: "Không thể tạo folder")
                },
                { folder ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            folders = it.folders + folder,
                            successMessage = "Folder đã được tạo thành công"
                        )
                    }
                    onSuccess(folder)
                }
            )
        }
    }

    /**
     * Updates an existing folder.
     */
    private fun updateFolder(
        id: Int,
        request: UpdateFolderRequest
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            exerciseRepository.updateFolder(id, request).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { updatedFolder ->
                    _state.update { state ->
                        val updatedFolders = state.folders.map {
                            if (it.id == id) updatedFolder else it
                        }

                        state.copy(
                            isLoading = false,
                            folders = updatedFolders,
                            currentFolder = if (state.currentFolder?.id == id) updatedFolder else state.currentFolder,
                            successMessage = "Folder updated successfully"
                        )
                    }
                }
            )
        }
    }

    /**
     * Deletes a folder.
     */
    private fun deleteFolder(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            exerciseRepository.deleteFolder(id).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { _ ->
                    _state.update { state ->
                        state.copy(
                            isLoading = false,
                            folders = state.folders.filter { it.id != id },
                            currentFolder = if (state.currentFolder?.id == id) null else state.currentFolder,
                            successMessage = "Folder deleted successfully"
                        )
                    }
                }
            )
        }
    }

    /**
     * Votes for a folder.
     */
    private fun voteFolder(id: Int) {
        viewModelScope.launch {
            exerciseRepository.voteFolder(id).fold(
                { failure ->
                    _state.update { it.copy(error = failure) }
                },
                { _ ->
                    // Update the vote count in the UI state
                    _state.update { state ->
                        val updatedFolders = state.folders.map { folder ->
                            if (folder.id == id) {
                                folder.copy(voteCount = folder.voteCount + 1,
                                    isAlreadyVote = true)
                            } else {
                                folder
                            }
                        }

                        val updatedCurrentFolder = state.currentFolder?.let { folder ->
                            if (folder.id == id) {
                                folder.copy(voteCount = folder.voteCount + 1,
                                    isAlreadyVote = true)
                            } else {
                                folder
                            }
                        }

                        state.copy(
                            folders = updatedFolders,
                            currentFolder = updatedCurrentFolder
                        )
                    }
                }
            )
        }
    }

    /**
     * Removes a vote from a folder.
     */
    private fun unvoteFolder(id: Int) {
        viewModelScope.launch {
            exerciseRepository.unVoteFolder(id).fold(
                { failure ->
                    _state.update { it.copy(error = failure) }
                },
                { _ ->
                    // Update the vote count in the UI state
                    _state.update { state ->
                        val updatedFolders = state.folders.map { folder ->
                            if (folder.id == id) {
                                folder.copy(voteCount = (folder.voteCount - 1).coerceAtLeast(0),
                                    isAlreadyVote = false)
                            } else {
                                folder
                            }
                        }

                        val updatedCurrentFolder = state.currentFolder?.let { folder ->
                            if (folder.id == id) {
                                folder.copy(voteCount = (folder.voteCount - 1).coerceAtLeast(0),
                                    isAlreadyVote = false)
                            } else {
                                folder
                            }
                        }

                        state.copy(
                            folders = updatedFolders,
                            currentFolder = updatedCurrentFolder
                        )
                    }
                }
            )
        }
    }

    /**
     * Creates a new comment.
     */
    private fun createComment(folderId: Int, content: String, parentId: Int?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Kiểm tra nội dung comment không null và không trống
            if (content.isBlank()) {
                _state.update { it.copy(
                    isLoading = false,
                    error = AppFailure(error = AppError.Unknown, message = "Nội dung comment không được để trống")
                ) }
                return@launch
            }
            exerciseRepository.createComment(folderId, content, parentId).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { comment ->
                    _state.update { state ->
                        if (parentId == null) {
                            // Top-level comment
                            state.copy(
                                isLoading = false,
                                comments = state.comments + comment,
                                successMessage = "Comment added successfully"
                            )
                        } else {
                            // Child comment
                            val updatedChildComments = state.childComments.toMutableMap().apply {
                                val existingComments = getOrDefault(parentId, emptyList())
                                put(parentId, existingComments + comment)
                            }

                            state.copy(
                                isLoading = false,
                                childComments = updatedChildComments,
                                successMessage = "Reply added successfully"
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Fetches child comments for a parent comment.
     */
    private fun getChildComments(folderId: Int, parentId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            exerciseRepository.getChildComments(folderId, parentId).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { comments ->
                    _state.update { state ->
                        val updatedChildComments = state.childComments.toMutableMap().apply {
                            put(parentId, comments)
                        }

                        state.copy(
                            isLoading = false,
                            childComments = updatedChildComments
                        )
                    }
                }
            )
        }
    }

    /**
     * Updates an existing comment.
     */
    private fun updateComment(folderId: Int, commentId: Int, content: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Kiểm tra nội dung comment không null và không trống
            if (content.isBlank()) {
                _state.update { it.copy(
                    isLoading = false,
                    error = AppFailure(error = AppError.Unknown, message = "Nội dung comment không được để trống")
                ) }
                return@launch
            }
            exerciseRepository.updateComment(folderId, commentId, content).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { updatedComment ->
                    _state.update { state ->
                        // Check if it's a top-level comment
                        val isTopLevel = state.comments.any { it.id == commentId }

                        if (isTopLevel) {
                            // Update in top-level comments
                            val updatedComments = state.comments.map {
                                if (it.id == commentId) updatedComment else it
                            }

                            state.copy(
                                isLoading = false,
                                comments = updatedComments,
                                successMessage = "Comment updated successfully"
                            )
                        } else {
                            // Update in child comments
                            val updatedChildComments =
                                state.childComments.mapValues { (_, comments) ->
                                    comments.map {
                                        if (it.id == commentId) updatedComment else it
                                    }
                                }

                            state.copy(
                                isLoading = false,
                                childComments = updatedChildComments,
                                successMessage = "Comment updated successfully"
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Deletes a comment.
     */
    private fun deleteComment(folderId: Int, commentId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            exerciseRepository.deleteComment(folderId, commentId).fold(
                { failure ->
                    _state.update { it.copy(isLoading = false, error = failure) }
                },
                { _ ->
                    _state.update { state ->
                        // Check if it's a top-level comment
                        val isTopLevel = state.comments.any { it.id == commentId }

                        if (isTopLevel) {
                            // Remove from top-level comments and any associated child comments
                            val updatedComments = state.comments.filter { it.id != commentId }
                            val updatedChildComments = state.childComments.toMutableMap().apply {
                                remove(commentId)
                            }

                            state.copy(
                                isLoading = false,
                                comments = updatedComments,
                                childComments = updatedChildComments,
                                successMessage = "Comment deleted successfully"
                            )
                        } else {
                            // Remove from child comments
                            val updatedChildComments =
                                state.childComments.mapValues { (_, comments) ->
                                    comments.filter { it.id != commentId }
                                }

                            state.copy(
                                isLoading = false,
                                childComments = updatedChildComments,
                                successMessage = "Comment deleted successfully"
                            )
                        }
                    }
                }
            )
        }
    }

    /**
     * Cập nhật thông tin của một quiz
     */
    private fun updateQuiz(quizId: Int, title: String, questions: List<Question>) {
        _state.update { state ->
            val currentFolder = state.currentFolder ?: return

            // Tìm và cập nhật quiz trong folder
            val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                if (quiz.id == quizId) {
                    quiz.copy(title = title, question = questions)
                } else {
                    quiz
                }
            } ?: emptyList()

            // Cập nhật currentQuiz nếu đang xem quiz này
            val updatedCurrentQuiz = if (state.currentQuiz?.id == quizId) {
                state.currentQuiz.copy(title = title, question = questions)
            } else {
                state.currentQuiz
            }

            // Cập nhật folder với quizzes mới
            val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

            state.copy(
                currentFolder = updatedFolder,
                currentQuiz = updatedCurrentQuiz
            )
        }
    }

    /**
     * Thêm câu hỏi mới vào quiz
     */
    private fun addQuestionToQuiz(quizId: Int, question: Question) {
        _state.update { state ->
            val currentFolder = state.currentFolder ?: return
            val currentQuiz = state.currentQuiz

            if (currentQuiz != null && currentQuiz.id == quizId) {
                // Nếu đang xem quiz này, cập nhật currentQuiz
                val updatedQuestions = currentQuiz.question + question
                val updatedQuiz = currentQuiz.copy(question = updatedQuestions)

                // Cập nhật quiz trong folder
                val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                    if (quiz.id == quizId) updatedQuiz else quiz
                } ?: listOf(updatedQuiz)

                val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

                state.copy(
                    currentFolder = updatedFolder,
                    currentQuiz = updatedQuiz,
                    successMessage = "Đã thêm câu hỏi mới"
                )
            } else {
                // Nếu không phải quiz hiện tại, chỉ cập nhật trong folder
                val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                    if (quiz.id == quizId) {
                        quiz.copy(question = quiz.question + question)
                    } else {
                        quiz
                    }
                } ?: emptyList()

                val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

                state.copy(
                    currentFolder = updatedFolder,
                    successMessage = "Đã thêm câu hỏi mới"
                )
            }
        }
    }

    /**
     * Cập nhật một câu hỏi trong quiz
     */
    private fun updateQuestionInQuiz(quizId: Int, questionIndex: Int, question: Question) {
        _state.update { state ->
            val currentFolder = state.currentFolder ?: return
            val currentQuiz = state.currentQuiz

            if (currentQuiz != null && currentQuiz.id == quizId) {
                // Nếu đang xem quiz này, cập nhật currentQuiz
                val updatedQuestions = currentQuiz.question.toMutableList().apply {
                    if (questionIndex in indices) {
                        this[questionIndex] = question
                    }
                }
                val updatedQuiz = currentQuiz.copy(question = updatedQuestions)

                // Cập nhật quiz trong folder
                val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                    if (quiz.id == quizId) updatedQuiz else quiz
                } ?: listOf(updatedQuiz)

                val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

                state.copy(
                    currentFolder = updatedFolder,
                    currentQuiz = updatedQuiz,
                    successMessage = "Đã cập nhật câu hỏi"
                )
            } else {
                // Nếu không phải quiz hiện tại, chỉ cập nhật trong folder
                val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                    if (quiz.id == quizId) {
                        val updatedQuestions = quiz.question.toMutableList().apply {
                            if (questionIndex in indices) {
                                this[questionIndex] = question
                            }
                        }
                        quiz.copy(question = updatedQuestions)
                    } else {
                        quiz
                    }
                } ?: emptyList()

                val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

                state.copy(
                    currentFolder = updatedFolder,
                    successMessage = "Đã cập nhật câu hỏi"
                )
            }
        }
    }

    /**
     * Xóa một câu hỏi khỏi quiz
     */
    private fun deleteQuestionFromQuiz(quizId: Int, questionIndex: Int) {
        _state.update { state ->
            val currentFolder = state.currentFolder ?: return
            val currentQuiz = state.currentQuiz

            if (currentQuiz != null && currentQuiz.id == quizId) {
                // Nếu đang xem quiz này, cập nhật currentQuiz
                val updatedQuestions = currentQuiz.question.toMutableList().apply {
                    if (questionIndex in indices) {
                        removeAt(questionIndex)
                    }
                }
                val updatedQuiz = currentQuiz.copy(question = updatedQuestions)

                // Cập nhật quiz trong folder
                val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                    if (quiz.id == quizId) updatedQuiz else quiz
                } ?: listOf(updatedQuiz)

                val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

                state.copy(
                    currentFolder = updatedFolder,
                    currentQuiz = updatedQuiz,
                    successMessage = "Đã xóa câu hỏi"
                )
            } else {
                // Nếu không phải quiz hiện tại, chỉ cập nhật trong folder
                val updatedQuizzes = currentFolder.quizzes?.map { quiz ->
                    if (quiz.id == quizId) {
                        val updatedQuestions = quiz.question.toMutableList().apply {
                            if (questionIndex in indices) {
                                removeAt(questionIndex)
                            }
                        }
                        quiz.copy(question = updatedQuestions)
                    } else {
                        quiz
                    }
                } ?: emptyList()

                val updatedFolder = currentFolder.copy(quizzes = updatedQuizzes)

                state.copy(
                    currentFolder = updatedFolder,
                    successMessage = "Đã xóa câu hỏi"
                )
            }
        }
    }

    /**
     * Lưu folder cùng với các quiz đã cập nhật
     */
    private fun saveFolderWithQuizzes(folderId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val currentFolder = _state.value.currentFolder ?: return@launch

            // Chuyển đổi từ domain model sang request model
            val quizRequests = currentFolder.quizzes?.map { quiz ->
                QuizRequest(
                    title = quiz.title,
                    question = quiz.question.map { question ->
                        QuestionRequest(
                            question = question.question,
                            options = question.options,
                            correctAnswers = question.correctAnswers,
                            explanation = question.explanation,
                            image = question.image,
                            time = question.time,
                            type = question.type
                        )
                    }
                )
            } ?: emptyList()

            val updateRequest = UpdateFolderRequest(
                name = currentFolder.name,
                quizzes = quizRequests,
                flashCards = null // Giữ nguyên flashcards
            )

            exerciseRepository.updateFolder(folderId, updateRequest).fold(
                { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = failure,
                            successMessage = null
                        )
                    }
                },
                { updatedFolder ->
                    _state.update { state ->
                        val updatedFolders = state.folders.map {
                            if (it.id == folderId) updatedFolder else it
                        }

                        state.copy(
                            isLoading = false,
                            folders = updatedFolders,
                            currentFolder = updatedFolder,
                            successMessage = "Folder đã được cập nhật thành công"
                        )
                    }
                }
            )
        }
    }
    fun ensureQuizExists() {
        _state.update { state ->
            val currentFolder = state.currentFolder
            if (currentFolder != null) {
                val quizzes = currentFolder.quizzes ?: emptyList()
                if (quizzes.isEmpty()) {
                    // Tạo quiz mới
                    val newQuiz = Quiz(
                        id = 1,
                        title = "Quiz mới",
                        question = emptyList()
                    )
                    val updatedFolder = currentFolder.copy(quizzes = listOf(newQuiz))
                    state.copy(
                        currentFolder = updatedFolder,
                        currentQuiz = newQuiz,
                        successMessage = "Đã tạo quiz mới vì folder chưa có quiz"
                    )
                } else {
                    state
                }
            } else {
                state
            }
        }
    }

    fun clearSuccessMessage() {
        _state.update { it.copy(successMessage = null) }
    }
}