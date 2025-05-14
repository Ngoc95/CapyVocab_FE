package com.example.capyvocab_fe.admin.word.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.domain.repository.AdminTopicRepository
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordBody
import com.example.capyvocab_fe.admin.word.data.remote.model.CreateWordRequest
import com.example.capyvocab_fe.admin.word.data.remote.model.UpdateWordRequest
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
    private val wordRepository: AdminWordRepository,
    private val topicRepository: AdminTopicRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WordListState())
    val state: StateFlow<WordListState> = _state

    fun onEvent(event: WordEvent) {
        when (event) {
            is WordEvent.LoadAllWords -> loadAllWords()
            is WordEvent.LoadMoreAllWords -> loadAllWords(loadMore = true)
            is WordEvent.LoadWords -> loadWords(event.topic)
            is WordEvent.LoadMoreWords -> loadWords(event.topic, loadMore = true)
            is WordEvent.UpdateWord -> updateWord(event.word)
            is WordEvent.DeleteWord -> deleteWord(event.wordId)
            is WordEvent.CreateWord -> createWord(event.topicId, event.word)
            is WordEvent.CancelMultiSelect -> cancelMultiSelect()
            is WordEvent.OnDeleteSelectedWords -> deleteSelectedTopics()
            is WordEvent.OnSelectAllToggle -> selectAll()
            is WordEvent.OnWordLongPress -> startMultiSelect(event.wordId)
            is WordEvent.OnWordSelectToggle -> toggleWordSelection(event.wordId)
        }
    }

    private fun loadAllWords(loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            wordRepository.getAllWords(nextPage)
                .onRight { newWords ->
                    _state.update {
                        val allWords = if (loadMore) it.words + newWords else newWords
                        it.copy(
                            isLoading = false,
                            words = allWords,
                            currentPage = nextPage,
                            isEndReached = allWords.isEmpty()
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "Failed to load all words"
                        )
                    }
                }
        }
    }

    private fun loadWords(topic: Topic, loadMore: Boolean = false) {
        viewModelScope.launch {
            val nextPage = if (loadMore) state.value.currentPage + 1 else 1

            _state.update { it.copy(isLoading = false, errorMessage = "") }

            topicRepository.getTopicWords(topic.id, nextPage)
                .onRight { newWords ->
                    _state.update {
                        val allWords = if (loadMore) state.value.words + newWords else newWords
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
                            errorMessage = failure.message ?: "failed to load words"
                        )
                    }
                }
        }
    }

    private fun createWord(topicId: Int, word: Word) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val createWordRequest = CreateWordRequest(
                words = listOf(
                    CreateWordBody(
                        content = word.content,
                        pronunciation = word.pronunciation,
                        position = word.position,
                        meaning = word.meaning,
                        rank = word.rank,
                        audio = word.audio,
                        image = word.image,
                        example = word.example,
                        translateExample = word.translateExample,
                        topicIds = listOf(topicId)
                    )
                )
            )
            wordRepository.createWords(createWordRequest)
                .onRight { newWord ->
                    _state.update { currentState ->
                        val updatedWords = currentState.words + newWord
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "",
                            words = updatedWords
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to create word"
                        )
                    }
                }
        }
    }

    private fun updateWord(word: Word) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val updateWordReq = UpdateWordRequest(
                content = word.content,
                pronunciation = word.pronunciation,
                position = word.position,
                meaning = word.meaning,
                rank = word.rank,
                audio = word.audio,
                image = word.image,
                example = word.example,
                translateExample = word.translateExample
            )

            wordRepository.updateWord(word.id, updateWordReq)
                .onRight { updatedWord ->
                    _state.update { currentState ->
                        val updatedWords = currentState.words.map {
                            if (it.id == updatedWord.id) updatedWord else it
                        }
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "",
                            words = updatedWords
                        )
                    }
                }
                .onLeft { failure ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = failure.message ?: "failed to update word"
                        )
                    }
                }
        }
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

    private fun startMultiSelect(wordId: Int) {
        _state.update {
            it.copy(
                isMultiSelecting = true,
                selectedWords = setOf(wordId)
            )
        }
    }

    private fun cancelMultiSelect() {
        _state.update {
            it.copy(
                isMultiSelecting = false,
                isSelectAll = false,
                selectedWords = emptySet()
            )
        }
    }

    private fun selectAll() {
        _state.update { currentState ->
            val allSelected = currentState.isSelectAll
            currentState.copy(
                selectedWords = if (allSelected) emptySet() else currentState.words.map { it.id }
                    .toSet(),
                isSelectAll = !allSelected
            )
        }
    }

    private fun toggleWordSelection(wordId: Int) {
        val currentSelected = _state.value.selectedWords.toMutableSet()
        if (currentSelected.contains(wordId)) {
            currentSelected.remove(wordId)
        } else {
            currentSelected.add(wordId)
        }
        _state.update { currentState ->
            currentState.copy(
                selectedWords = currentSelected,
                isSelectAll = currentSelected.size == currentState.words.size
            )
        }
    }

    private fun deleteSelectedTopics() {

    }

}
