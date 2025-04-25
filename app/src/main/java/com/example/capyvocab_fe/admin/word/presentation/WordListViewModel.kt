package com.example.capyvocab_fe.admin.word.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.word.domain.repository.AdminWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordListViewModel @Inject constructor(
    private val adminWordRepository: AdminWordRepository
): ViewModel() {

    private val _state = MutableStateFlow(WordListState())
    val state : StateFlow<WordListState> = _state

    fun loadWords() {
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

}