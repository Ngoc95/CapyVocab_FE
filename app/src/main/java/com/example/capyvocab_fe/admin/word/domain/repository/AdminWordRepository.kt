package com.example.capyvocab_fe.admin.word.domain.repository

import arrow.core.Either
import com.example.capyvocab_fe.admin.user.domain.error.AdminFailure
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.DeleteResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.RestoreResponse
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word

interface AdminWordRepository {
    suspend fun createWords(createWordRequest: CreateWordRequest): Either<AdminFailure, List<Word>>
    suspend fun getAllWords(page: Int): Either<AdminFailure, List<Word>>
    suspend fun getWordById(id: Int): Either<AdminFailure, Word>
    suspend fun updateWord(
        id: Int,
        updateWordRequest: UpdateWordRequest
    ): Either<AdminFailure, Word>

    suspend fun deleteWord(id: Int): Either<AdminFailure, DeleteResponse>
    suspend fun restoreWord(id: Int): Either<AdminFailure, RestoreResponse>
}