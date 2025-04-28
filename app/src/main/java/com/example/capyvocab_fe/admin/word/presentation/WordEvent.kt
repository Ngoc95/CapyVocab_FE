package com.example.capyvocab_fe.admin.word.presentation

import com.example.capyvocab_fe.admin.word.domain.model.Word

sealed class WordEvent {
    data class LoadWords(val topicId: Int) : WordEvent()
    data class CreateWord(val word: Word) : WordEvent()
    data class UpdateWord(val word: Word) : WordEvent()
    data class DeleteWord(val wordId: Int) : WordEvent()
}
