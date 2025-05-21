package com.example.capyvocab_fe.user.test.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
import com.example.capyvocab_fe.user.test.data.remote.ExerciseApi
import com.example.capyvocab_fe.user.test.data.remote.model.CreateCommentRequest
import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateCommentRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Comment
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.user.test.domain.repository.ExerciseRepository
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val api: ExerciseApi
) : ExerciseRepository {
    override suspend fun getAllFolders(
        page: Int,
        limit: Int,
        name: String?,
        code: String?
    ): Either<AppFailure, List<Folder>> {
        return Either.catch {
            api.getAllFolders(page, limit, name, code).metaData.folders
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getFolderById(id: Int): Either<AppFailure, Folder> {
        return Either.catch {
            api.getFolderById(id).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun createFolder(body: CreateFolderRequest): Either<AppFailure, Folder> {
        return Either.catch {
            api.createFolder(body).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun updateFolder(
        id: Int,
        body: UpdateFolderRequest
    ): Either<AppFailure, Folder> {
        return Either.catch {
            api.updateFolder(id, body).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun deleteFolder(id: Int): Either<AppFailure, Unit> {
        return Either.catch {
            api.deleteFolder(id)
            Unit
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun voteFolder(id: Int): Either<AppFailure, Unit> {
        return Either.catch {
            api.voteFolder(id)
            Unit
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun unVoteFolder(id: Int): Either<AppFailure, Unit> {
        return Either.catch {
            api.unVoteFolder(id)
            Unit
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getFolderComments(folderId: Int): Either<AppFailure, List<Comment>> {
        return Either.catch {
            api.getFolderComments(folderId).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun createComment(
        folderId: Int,
        content: String,
        parentId: Int?
    ): Either<AppFailure, Comment> {
        return Either.catch {
            val request = CreateCommentRequest(content, parentId)
            api.createComment(folderId, request).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getChildComments(
        folderId: Int,
        parentId: Int
    ): Either<AppFailure, List<Comment>> {
        return Either.catch {
            api.getChildComments(folderId, parentId).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun updateComment(
        folderId: Int,
        commentId: Int,
        content: String
    ): Either<AppFailure, Comment> {
        return Either.catch {
            val request = UpdateCommentRequest(content)
            api.updateComment(folderId, commentId, request).metaData
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun deleteComment(
        folderId: Int,
        commentId: Int
    ): Either<AppFailure, Unit> {
       return Either.catch {
           api.deleteComment(folderId, commentId)
           Unit
       }.mapLeft { it.toAppFailure() }
    }
}