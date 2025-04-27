package com.example.capyvocab_fe.admin.word.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val adminWordRepository: AdminWordRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WordListState())
    val state: StateFlow<WordListState> = _state

    fun onEvent(event: WordEvent) {
        when (event) {
            is WordEvent.LoadWords -> loadWordsByTopic(event.topicId)
            is WordEvent.CreateWord -> createWord(event.word)
            is WordEvent.UpdateWord -> updateWord(event.word)
            is WordEvent.DeleteWord -> deleteWord(event.wordId)
        }
    }

    private fun loadWords() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            adminWordRepository.getAllWords()
                .onRight { words ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        words = words,
                        error = null
                    )
                }
                .onLeft { failure ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = failure.message ?: "Unknown error"
                    )
                }
        }
    }

    private fun createWord(word: Word) {
//        viewModelScope.launch {
//            adminWordRepository.createWord(word)
//                .onRight {
//                    onEvent(WordEvent.Load) // Reload lại danh sách
//                }
//                .onLeft { failure ->
//                    _state.value = _state.value.copy(
//                        error = failure.message ?: "Error creating word"
//                    )
//                }
//        }
    }

    private fun updateWord(word: Word) {
//        viewModelScope.launch {
//            adminWordRepository.updateWord(word)
//                .onRight {
//                    onEvent(WordEvent.Load) // Reload
//                }
//                .onLeft { failure ->
//                    _state.value = _state.value.copy(
//                        error = failure.message ?: "Error updating word"
//                    )
//                }
//        }
    }

    private fun deleteWord(wordId: Int) {
//        viewModelScope.launch {
//            adminWordRepository.deleteWord(wordId)
//                .onRight {
//                    onEvent(WordEvent.Load) // Reload
//                }
//                .onLeft { failure ->
//                    _state.value = _state.value.copy(
//                        error = failure.message ?: "Error deleting word"
//                    )
//                }
//        }
    }

    private fun loadWordsByTopic(topicId: Int) {
//        _state.value = _state.value.copy(isLoading = true, error = null)
//        viewModelScope.launch {
//            adminWordRepository.getWordsByTopic(topicId)
//                .onRight { words ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        words = words,
//                        error = null
//                    )
//                }
//                .onLeft { failure ->
//                    _state.value = _state.value.copy(
//                        isLoading = false,
//                        error = failure.message ?: "Unknown error"
//                    )
//                }
//        }
    }
}
