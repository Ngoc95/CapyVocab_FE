package com.example.capyvocab_fe.admin.word.domain.repository

import android.net.Uri
import arrow.core.Either
import com.example.capyvocab_fe.core.error.AppFailure
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word

interface AdminWordRepository {
    suspend fun createWords(createWordRequest: CreateWordRequest): Either<AppFailure, List<Word>>
    suspend fun getAllWords(page: Int): Either<AppFailure, List<Word>>
    suspend fun getWordById(id: Int): Either<AppFailure, Word>
    suspend fun updateWord(
        id: Int,
        updateWordRequest: UpdateWordRequest
    ): Either<AppFailure, Word>

    suspend fun deleteWord(id: Int): Either<AppFailure, Unit>
    suspend fun uploadImage(uri: Uri): Either<AppFailure, String>
}