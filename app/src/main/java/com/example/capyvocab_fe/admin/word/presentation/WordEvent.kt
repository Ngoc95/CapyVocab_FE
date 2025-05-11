package com.example.capyvocab_fe.admin.word.presentation

import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word

sealed class WordEvent {
    data class LoadWords(val topic: Topic) : WordEvent()
    data class LoadMoreWords(val topic: Topic): WordEvent()
    data class UpdateWord(val word: Word): WordEvent()
    data class CreateWord(val topic: Topic, val word: Word): WordEvent()
    data class DeleteWord(val wordId: Int) : WordEvent()

    data class OnWordLongPress(val wordId: Int) : WordEvent()
    data class OnWordSelectToggle(val wordId: Int) : WordEvent()
    object OnSelectAllToggle : WordEvent()
    object OnDeleteSelectedWords : WordEvent()
    object CancelMultiSelect : WordEvent()
}
