package com.example.capyvocab_fe.user.review.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.user.review.data.remote.model.WordProgressUpdate
import com.example.capyvocab_fe.user.review.domain.usecase.GetProgressSummaryUseCase
import com.example.capyvocab_fe.user.review.domain.usecase.GetReviewWordsUseCase
import com.example.capyvocab_fe.user.review.domain.usecase.UpdateWordProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewWordsUseCase: GetReviewWordsUseCase,
    private val updateWordProgressUseCase: UpdateWordProgressUseCase,
    private val getProgressSummaryUseCase: GetProgressSummaryUseCase
) : ViewModel() {

    var state by mutableStateOf(ReviewState())
        private set

    var allWords: List<Word> = emptyList()
    private val _progress = mutableMapOf<Int, WordProgressUpdate>()
    private val wordQueue = mutableListOf<Word>() // để thêm lại từ sai

    @RequiresApi(Build.VERSION_CODES.O)
    fun onEvent(event: ReviewEvent) {
        when (event) {
            is ReviewEvent.LoadWords -> loadWords()
            is ReviewEvent.StartReview -> startReview()
            is ReviewEvent.SubmitAnswer -> submitAnswer(event.answer)
            is ReviewEvent.Continue -> continueReview()
            is ReviewEvent.Complete -> completeReview()
            is ReviewEvent.LoadProgressSummary -> loadProgressSummary()
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            runCatching { getReviewWordsUseCase() }
                .onSuccess { result ->
                    allWords = result.words
                    wordQueue.clear()
                    wordQueue.addAll(result.words)

                    state = state.copy(
                        words = result.words.takeIf { it.isNotEmpty() }?.let { listOf(it.first()) } ?: emptyList(),
                        preparedCount = result.words.size,
                        totalWordsToReview = result.total,
                        isLoading = false,
                        isEmpty = result.words.isEmpty(),
                        currentIndex = 0
                    )
                }
                .onFailure {
                    state = state.copy(isLoading = false, error = it.message)
                }
        }
    }


    private fun startReview() {
        state = state.copy(hasStarted = true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun submitAnswer(answer: String) {
        val currentWord = wordQueue.firstOrNull() ?: return

        val isCorrect = answer.trim().lowercase() == state.currentCorrectAnswer

        val selectedAnswerBoolean = answer == "Đúng"


        val existing = _progress[currentWord.id]
        val updatedWrongCount = if (isCorrect) {
            existing?.wrongCount ?: 0
        } else {
            (existing?.wrongCount ?: 0) + 1
        }

        _progress[currentWord.id] = WordProgressUpdate(
            wordId = currentWord.id,
            wrongCount = updatedWrongCount,
            reviewedDate = Instant.now().toString()
        )

        if (!isCorrect) {
            // cho từ sai quay lại cuối hàng đợi
            wordQueue.add(currentWord)
        }

        state = state.copy(
            selectedAnswer = answer,
            selectedAnswerBoolean = selectedAnswerBoolean,
            isCorrectAnswer = isCorrect,
            showFeedback = true,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun continueReview() {
        if (wordQueue.isNotEmpty()) {
            wordQueue.removeAt(0)
        }

        if (wordQueue.isEmpty()) {
            onEvent(ReviewEvent.Complete)
        } else {
            state = state.copy(
                words = listOf(wordQueue.first()),
                currentIndex = state.currentIndex + 1,
                showFeedback = false,
                selectedAnswer = null,
                questionSessionId = UUID.randomUUID().toString()
            )
        }
    }

    private fun completeReview() {
        viewModelScope.launch {
            val totalUpdated = updateWordProgressUseCase(_progress.values.toList())
            state = state.copy(completed = true, totalUpdated = totalUpdated)
        }
    }

    fun setCorrectAnswer(correct: String) {
        state = state.copy(currentCorrectAnswer = correct.trim().lowercase())
    }

    fun loadProgressSummary() {
        viewModelScope.launch {
            runCatching { getProgressSummaryUseCase() }
                .onSuccess { response ->
                    val chartItems = response.statistics.orEmpty().map {
                        ProgressChartItem(label = it.level, count = it.wordCount)
                    }

                    state = state.copy(
                        progressChart = chartItems,
                        totalLearnWord = response.totalLearnWord ?: 0
                    )
                }
                .onFailure {

                }
        }
    }

    fun reset() {
        state = ReviewState()
        _progress.clear()
        wordQueue.clear()
    }
}
