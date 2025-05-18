package com.example.capyvocab_fe.admin.word.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.topic.data.remote.model.UpdateTopicRequest
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
            is WordEvent.UpdateWord -> updateWord(event.word, event.imageUri)
            is WordEvent.DeleteWord -> deleteWord(event.wordId)
            is WordEvent.CreateWord -> createWord(event.topicId, event.word, event.imageUri)
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
            _state.update { it.copy(isLoading = true, errorMessage = "", currentTopic = null) }

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

            _state.update { it.copy(isLoading = false, errorMessage = "", currentTopic = topic) }

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

    private fun createWord(topicId: Int, word: Word, imageUri: Uri?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }
            val imageUrl = uploadImageIfNeeded(imageUri, word.image) ?: return@launch

            val createWordRequest = CreateWordRequest(
                words = listOf(
                    CreateWordBody(
                        content = word.content,
                        pronunciation = word.pronunciation,
                        position = word.position,
                        meaning = word.meaning,
                        audio = word.audio,
                        image = imageUrl,
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

    private fun updateWord(word: Word, imageUri: Uri?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            val imageUrl = uploadImageIfNeeded(imageUri, word.image) ?: return@launch

            val updateWordReq = UpdateWordRequest(
                content = word.content,
                pronunciation = word.pronunciation,
                position = word.position,
                meaning = word.meaning,
                audio = word.audio,
                image = imageUrl,
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
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = "") }

            // Check if we're in a topic context or main word screen
            val currentTopic = state.value.currentTopic

            if (currentTopic != null) {
                // Case 2: In WordsInTopicScreen - only remove word from topic
                // Get current topic's words excluding the one to delete
                val updatedWordIds = currentTopic.words
                    .filterNot { it.id == wordId }
                    .map { it.id }

                // Create update request with the filtered word list
                val updateTopicRequest = UpdateTopicRequest(
                    title = currentTopic.title,
                    description = currentTopic.description,
                    thumbnail = currentTopic.thumbnail,
                    type = currentTopic.type,
                    wordIds = updatedWordIds
                )

                // Update the topic with the new word list
                topicRepository.updateTopic(currentTopic.id, updateTopicRequest)
                    .onRight {
                        _state.update { currentState ->
                            val updatedWords = currentState.words.filterNot { it.id == wordId }
                            currentState.copy(
                                isLoading = false,
                                words = updatedWords
                            )
                        }
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Không thể xoá từ khỏi chủ đề"
                            )
                        }
                    }
            } else {
                // Case 1: In WordScreen - delete word from database
                wordRepository.deleteWord(wordId)
                    .onRight {
                        _state.update { currentState ->
                            val updatedWords = currentState.words.filterNot { it.id == wordId }
                            currentState.copy(
                                isLoading = false,
                                words = updatedWords
                            )
                        }
                    }
                    .onLeft { failure ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = failure.message ?: "Không thể xoá từ"
                            )
                        }
                    }
            }
        }
    }

    private fun deleteSelectedTopics() {
        viewModelScope.launch {
            val selectedIds = state.value.selectedWords
            if (selectedIds.isEmpty()) return@launch

            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = ""
                )
            }

            // Check if we're in a topic context or main word screen
            val currentTopic = state.value.currentTopic
            var hasError = false

            if (currentTopic != null) {
                // Case 2: In WordsInTopicScreen - only remove words from topic
                // Get current topic's words excluding the ones to delete
                val updatedWordIds = currentTopic.words
                    .filterNot { it.id in selectedIds }
                    .map { it.id }

                // Create update request with the filtered word list
                val updateTopicRequest = UpdateTopicRequest(
                    title = currentTopic.title,
                    description = currentTopic.description,
                    thumbnail = currentTopic.thumbnail,
                    type = currentTopic.type,
                    wordIds = updatedWordIds
                )

                // Update the topic with the new word list
                topicRepository.updateTopic(currentTopic.id, updateTopicRequest)
                    .onLeft { hasError = true }
            } else {
                // Case 1: In WordScreen - delete words from database
                selectedIds.forEach { wordId ->
                    wordRepository.deleteWord(wordId)
                        .onLeft { hasError = true }
                }
            }

            // Update UI state
            val remainingWords = state.value.words.filterNot { it.id in selectedIds }

            _state.update {
                it.copy(
                    words = remainingWords,
                    selectedWords = emptySet(),
                    isMultiSelecting = false,
                    isLoading = false,
                    errorMessage = if (hasError) "Một số từ không thể xoá" else ""
                )
            }
        }
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

    private suspend fun uploadImageIfNeeded(uri: Uri?, currentImage: String?): String? {
        if (uri == null) return currentImage
        val uploadResult = wordRepository.uploadImage(uri)
        return uploadResult.fold(
            ifLeft = { failure ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Upload ảnh thất bại: ${failure.message}"
                    )
                }
                null
            },
            ifRight = { url -> url }
        )
    }
}
