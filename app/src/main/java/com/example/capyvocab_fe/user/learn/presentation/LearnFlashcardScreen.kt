package com.example.capyvocab_fe.user.learn.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.core.ui.components.BottomFeedbackCard
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.user.learn.presentation.components.FlashcardView
import com.example.capyvocab_fe.user.learn.presentation.components.TypingView

@Composable
fun LearnFlashcardScreen(
    topic: Topic,
    viewModel: LearnViewModel = hiltViewModel(),
    onComplete: () -> Unit,
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var showExitConfirmation by remember { mutableStateOf(false) }

    LaunchedEffect(topic.id) {
        viewModel.onEvent(LearnEvent.ClearWords)
        viewModel.onEvent(LearnEvent.LoadWords(topic))
    }

    BackHandler {
        showExitConfirmation = true
    }

    // Hiển thị dialog xác nhận thoát
    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Quay lại") },
            text = { Text("Tiến trình học sẽ không được lưu. Bạn có chắc muốn thoát?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitConfirmation = false
                    navController.popBackStack("${Route.UserWordsScreen.route}/${topic.id}", inclusive = true)
                }) {
                    Text(text = "Thoát", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showExitConfirmation = false
                }) {
                    Text(text = "Hủy", color = Color.Black)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLearning -> {
                FlashcardView(
                    word = state.currentWord,
                    isFront = !state.isFlipped,
                    onFlip = { viewModel.onEvent(LearnEvent.FlipCard) },
                    onContinue = { viewModel.onEvent(LearnEvent.ContinueToTyping) },
                    onAlreadyKnow = { viewModel.onEvent(LearnEvent.AlreadyKnowWord) }
                )
            }

            state.isTyping && state.currentWord != null -> {
                TypingView(
                    word = state.currentWord!!,
                    onSubmit = { viewModel.onEvent(LearnEvent.SubmitAnswer(it)) },
                    showResult = state.showResult,
                )
            }
        }

        // Hiển thị feedback sau khi trả lời
        if (state.showResult && state.currentWord != null) {
            BottomFeedbackCard(
                isCorrect = state.answerResult == true,
                word = state.currentWord!!,
                onContinue = { viewModel.onEvent(LearnEvent.ContinueAfterResult) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

        if (state.showCompletionDialog) {
            AlertDialog(
                onDismissRequest = {},
                title = { Text("Hoàn thành") },
                text = { Text("Bạn đã hoàn thành chủ đề này!") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.onEvent(LearnEvent.DismissCompletionDialog)
                        onComplete()
                    }) {
                        Text("Tiếp tục")
                    }
                }
            )
        }
    }
}