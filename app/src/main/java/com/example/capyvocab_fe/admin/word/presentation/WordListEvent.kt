package com.example.capyvocab_fe.admin.word.presentation

import android.net.Uri
import com.example.capyvocab_fe.admin.word.domain.model.Word

sealed class WordListEvent {
    object LoadWords : WordListEvent()
    object LoadMoreWords : WordListEvent()
    data class SaveWord(
        val word: Word,
        val imageUri: Uri? = null,
        val audioUri: Uri? = null
    ) : WordListEvent()
    data class deleteWord(val wordId: Int): WordListEvent()
    data class restoreWord(val wordId: Int): WordListEvent()
}