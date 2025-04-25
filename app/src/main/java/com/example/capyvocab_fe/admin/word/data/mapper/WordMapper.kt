package com.example.capyvocab_fe.admin.word.data.mapper

import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word

fun Word.toCreateRequest(): CreateWordRequest = CreateWordRequest(
    content, pronunciation, position, meaning, audio, image, rank, example, translateExample
)

fun Word.toUpdateRequest(): UpdateWordRequest = UpdateWordRequest(
    content, pronunciation, position, meaning, audio, image, rank, example, translateExample
)