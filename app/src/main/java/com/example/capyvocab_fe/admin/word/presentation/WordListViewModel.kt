package com.example.capyvocab_fe.admin.word.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.word.data.mapper.toCreateRequest
import com.example.capyvocab_fe.admin.word.data.mapper.toUpdateRequest
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val adminWordRepository: AdminWordRepository
): ViewModel() {

    private val _state = MutableStateFlow(WordListState())
    val state : StateFlow<WordListState> = _state

    fun onEvent(event: WordListEvent) {
        when (event) {
            is WordListEvent.LoadWords -> loadWords()
            is WordListEvent.LoadMoreWords -> loadWords(loadMore = true)
            is WordListEvent.SaveWord -> saveWord(event.word)
            is WordListEvent.deleteWord -> deleteWord(event.wordId)
            is WordListEvent.restoreWord -> restoreWord(event.wordId)
        }
    }

    private fun loadWords(loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1

            _state.update { it.copy(isLoading = true, errorMessage = "") }
            adminWordRepository.getAllWords(nextPage)
                .onRight { newWords ->
                    _state.update {
                        val allWords = if (loadMore) it.words + newWords else newWords
                        it.copy(
                            isLoading = false,
                            words = allWords,
                            currentPage = nextPage,
                            isEndReached = newWords.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Đã xảy ra lỗi"
                        )
                    }
                }
        }
    }

    private fun saveWord(word: Word, audioUri: Uri?, imageUri: Uri?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val audioUrl = uploadAudioIfNeeded(audioUri, word.audio) ?: return@launch
            val imageUrl = uploadImageIfNeeded(imageUri, word.image) ?: return@launch

            val wordToSave = word.copy(audio = audioUrl, image = imageUrl)

            val result = if (word.id == 0) {
                // Create
                adminWordRepository.createWords(listOf(wordToSave.toCreateRequest()))
                    .map { it.firstOrNull() ?: wordToSave }
            } else {
                // Update
                adminWordRepository.updateWord(word.id, wordToSave.toUpdateRequest())
            }

            result.fold(
                ifLeft = { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Lỗi khi lưu từ")
                    }
                },
                ifRight = { updatedWord ->
                    _state.update { currentState ->
                        val updatedWords = currentState.words.map {
                            if (it.id == updatedWord.id) updatedWord else it
                        }
                        currentState.copy(
                            isLoading = false,
                            words = updatedWords.ifEmpty { listOf(updatedWord) },
                            errorMessage = ""
                        )
                    }
                }
            )
        }
    }

    private fun deleteWord(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = false, errorMessage = "") }

            adminWordRepository.deleteWord(id)
                .onRight {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            words = it.words.filterNot { word -> word.id == id }
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Lỗi khi xoá từ")
                    }
                }
        }
    }

    private fun restoreWord(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = false, errorMessage = "") }

            adminWordRepository.restoreWord(id)
                .onRight {
                    loadWords()
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(isLoading = false, errorMessage = failure.message ?: "Lỗi khi khôi phục từ")
                    }
                }
        }
    }

    private suspend fun updateImageIfNeeded(uri: Uri?, currentUrl: String?): String? {
        if (uri == null) return currentUrl
        return adminWordRepository.up
    }
}