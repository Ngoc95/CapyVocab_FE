package com.example.capyvocab_fe.user.test.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.auth.domain.error.AuthFailure
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Comment
import com.example.capyvocab_fe.user.test.domain.model.Folder

interface ExerciseRepository {
    suspend fun getUserInfo(): Either<AuthFailure, User>
    suspend fun getAllFolders(
        page: Int = 1,
        name: String? = null,
        code: String? = null
    ): Either<AppFailure, List<Folder>>
    suspend fun getFolderById(id: Int): Either<AppFailure, Folder>
    suspend fun createFolder(body: CreateFolderRequest): Either<AppFailure, Folder>
    suspend fun updateFolder(id: Int, body: UpdateFolderRequest): Either<AppFailure, Folder>
    suspend fun deleteFolder(id: Int): Either<AppFailure, Unit>
    suspend fun voteFolder(id: Int): Either<AppFailure, Unit>
    suspend fun unVoteFolder(id: Int): Either<AppFailure, Unit>
    suspend fun createComment(
        folderId: Int,
        content: String,
        parentId: Int? = null
    ): Either<AppFailure, Comment>
    suspend fun getChildComments(
        folderId: Int,
        parentId: Int
    ): Either<AppFailure, List<Comment>>
    suspend fun updateComment(
        folderId: Int,
        commentId: Int,
        content: String
    ): Either<AppFailure, Comment>
    suspend fun deleteComment(
        folderId: Int,
        commentId: Int
    ): Either<AppFailure, Unit>
    suspend fun uploadImage(uri: Uri): Either<AppFailure, String>
    suspend fun finishQuiz(folderId: Int, quizId: Int): Either<AppFailure, Unit>
}