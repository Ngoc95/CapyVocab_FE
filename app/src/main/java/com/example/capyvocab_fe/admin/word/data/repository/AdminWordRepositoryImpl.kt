package com.example.capyvocab_fe.admin.word.data.repository

import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.core.error.toAppFailure
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
) : AdminWordRepository {

    override suspend fun createWords(createWordRequest: CreateWordRequest): Either<AppFailure, List<Word>> {
        return Either.catch {
            adminWordApi.createWords(createWordRequest)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getAllWords(page: Int): Either<AppFailure, List<Word>> {
        return Either.catch {
            adminWordApi.getAllWords(page).metaData.words
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun getWordById(id: Int): Either<AppFailure, Word> {
        return Either.catch {
            adminWordApi.getWordById(id)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun updateWord(
        id: Int,
        updateWordRequest: UpdateWordRequest
    ): Either<AppFailure, Word> {
        return Either.catch {
            adminWordApi.updateWord(id, updateWordRequest)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun deleteWord(id: Int): Either<AppFailure, DeleteResponse> {
        return Either.catch {
            adminWordApi.deleteWordById(id)
        }.mapLeft { it.toAppFailure() }
    }

    override suspend fun restoreWord(id: Int): Either<AppFailure, RestoreResponse> {
        return Either.catch {
            adminWordApi.restoreWord(id)
        }.mapLeft { it.toAppFailure() }
    }
}