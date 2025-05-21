package com.example.capyvocab_fe.user.test.presentation.viewmodel

import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.domain.model.Question

sealed class ExerciseEvent {
    // Folder-related events
    data class GetAllFolders(
        val page: Int = 1,
        val limit: Int = 10,
        val name: String? = null,
        val code: String? = null
    ) : ExerciseEvent()
    data class GetFolderById(val id: Int) : ExerciseEvent()
    data class CreateFolder(
        val request: CreateFolderRequest,
        val onSuccess: (Folder) -> Unit = {},
        val onError: (String) -> Unit = {}
    ) : ExerciseEvent()
    data class UpdateFolder(val id: Int, val request: UpdateFolderRequest) : ExerciseEvent()
    data class DeleteFolder(val id: Int) : ExerciseEvent()
    data class VoteFolder(val id: Int) : ExerciseEvent()
    data class UnvoteFolder(val id: Int) : ExerciseEvent()

    // Comment-related events
    data class CreateComment(val folderId: Int, val content: String, val parentId: Int? = null) : ExerciseEvent()
    data class GetChildComments(val folderId: Int, val parentId: Int) : ExerciseEvent()
    data class UpdateComment(val folderId: Int, val commentId: Int, val content: String) : ExerciseEvent()
    data class DeleteComment(val folderId: Int, val commentId: Int) : ExerciseEvent()
    data class GetFolderComments(val folderId: Int) : ExerciseEvent() // New event for fetching folder comments

    data class UpdateQuiz(val quizId: Int, val title: String, val questions: List<Question>) : ExerciseEvent()
    data class AddQuestionToQuiz(val quizId: Int, val question: Question) : ExerciseEvent()
    data class UpdateQuestionInQuiz(val quizId: Int, val questionIndex: Int, val question: Question) : ExerciseEvent()
    data class DeleteQuestionFromQuiz(val quizId: Int, val questionIndex: Int) : ExerciseEvent()
    data class SaveFolderWithQuizzes(val folderId: Int) : ExerciseEvent()

    // Navigation events
    object NavigateToDoTest : ExerciseEvent()
    object NavigateToEnterCode : ExerciseEvent()
    object NavigateToCreatedTests : ExerciseEvent()
    object NavigateToCreateTest : ExerciseEvent()

    // UI state reset events
    object ResetError : ExerciseEvent()
    object ResetSuccess : ExerciseEvent()
}