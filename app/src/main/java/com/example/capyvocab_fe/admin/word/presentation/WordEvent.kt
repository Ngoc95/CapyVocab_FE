package com.example.capyvocab_fe.admin.word.presentation

import android.net.Uri
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word

sealed class WordEvent {
    object LoadAllWords : WordEvent()
    object LoadMoreAllWords : WordEvent()

    data class LoadWords(val topic: Topic) : WordEvent()
    data class LoadMoreWords(val topic: Topic) : WordEvent()

    data class UpdateWord(
        val word: Word,
        val imageUri: Uri? = null
    ) : WordEvent()
    data class CreateWord(
        val topicId: Int,
        val word: Word,
        val imageUri: Uri? = null
    ) : WordEvent()

    data class DeleteWord(val wordId: Int) : WordEvent()

    data class OnWordLongPress(val wordId: Int) : WordEvent()
    data class OnWordSelectToggle(val wordId: Int) : WordEvent()
    object OnSelectAllToggle : WordEvent()
    object OnDeleteSelectedWords : WordEvent()
    object CancelMultiSelect : WordEvent()
}
