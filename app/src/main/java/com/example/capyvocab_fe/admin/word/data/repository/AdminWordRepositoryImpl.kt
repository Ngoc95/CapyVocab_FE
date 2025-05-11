package com.example.capyvocab_fe.admin.word.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.user.domain.error.toAdminFailure
import com.example.capyvocab_fe.admin.word.data.remote.AdminWordApi
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.DeleteResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.RestoreResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import javax.inject.Inject

class AdminWordRepositoryImpl @Inject constructor(
    private val adminWordApi: AdminWordApi
): AdminWordRepository {

    override suspend fun createWords(createWordRequest: CreateWordRequest): Either<AdminFailure, List<Word>> {
        return Either.catch {
            adminWordApi.createWords(createWordRequest)
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun getAllWords(): Either<AdminFailure, List<Word>> {
        return Either.catch {
            adminWordApi.getAllWords().words
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun getWordById(id: Int): Either<AdminFailure, Word> {
        return Either.catch {
            adminWordApi.getWordById(id)
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun updateWord(id: Int, updateWordRequest: UpdateWordRequest): Either<AdminFailure, Word> {
        return Either.catch {
            adminWordApi.updateWord(id, updateWordRequest)
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun deleteWord(id: Int): Either<AdminFailure, DeleteResponse> {
        return Either.catch {
            adminWordApi.deleteWordById(id)
        }.mapLeft { it.toAdminFailure() }
    }

    override suspend fun restoreWord(id: Int): Either<AdminFailure, RestoreResponse> {
        return Either.catch {
            adminWordApi.restoreWord(id)
        }.mapLeft { it.toAdminFailure() }
    }
}