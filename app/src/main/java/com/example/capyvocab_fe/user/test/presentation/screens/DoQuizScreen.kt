package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.user.test.presentation.screens.components.OptionItem
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseState

/**
 * Màn hình làm bài kiểm tra
 * @param navController Navigation controller
 * @param quizId ID của bài kiểm tra
 * @param folderId ID của folder chứa bài kiểm tra
 * @param state Trạng thái hiện tại
 * @param onEvent Xử lý sự kiện
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoQuizScreen(
    navController: NavController,
    quizId: Int,
    folderId: Int,
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
) {
    val quiz = state.currentQuiz
    val questions = remember {
        (quiz?.question ?: emptyList()).shuffled()
    }

    // Nếu không có câu hỏi, hiển thị thông báo và quay lại
    if (questions.isEmpty()) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    // Vị trí câu hỏi hiện tại
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    // Danh sách câu trả lời đã chọn
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, List<String>>()) }

    // Trạng thái hiển thị kết quả
    var showResult by remember { mutableStateOf(false) }

    // Trạng thái đúng/sai của câu trả lời hiện tại
    var isCurrentAnswerCorrect by remember { mutableStateOf(false) }

    // Hiển thị dialog xác nhận thoát
    var showExitConfirmation by remember { mutableStateOf(false) }

    // Hiển thị dialog hoàn thành bài kiểm tra
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Số câu trả lời đúng
    var correctAnswersCount by remember { mutableIntStateOf(0) }

    // Focus manager để ẩn bàn phím khi cần
    val focusManager = LocalFocusManager.current

    // Xử lý khi người dùng nhấn nút Back
    LaunchedEffect(Unit) {
        // Có thể thêm logic để lưu trạng thái làm bài nếu cần
    }

    // Hàm kiểm tra đáp án
    fun checkAnswer() {
        if (currentQuestion == null) return

        val userAnswers = selectedAnswers[currentQuestionIndex] ?: emptyList()

        isCurrentAnswerCorrect = when (currentQuestion.type) {
            "FILL_IN_BLANK" -> {
                // Với câu hỏi điền vào chỗ trống, kiểm tra xem đáp án người dùng có trong danh sách đáp án đúng không
                val userAnswer = userAnswers.firstOrNull()?.trim() ?: ""
                currentQuestion.correctAnswers.any { it.equals(userAnswer, ignoreCase = true) }
            }

            "MULTIPLE_CHOICE" -> {
                // Với câu hỏi nhiều đáp án, kiểm tra xem người dùng đã chọn đúng tất cả các đáp án đúng chưa
                userAnswers.size == currentQuestion.correctAnswers.size &&
                        userAnswers.containsAll(currentQuestion.correctAnswers)
            }

            else -> {
                // Với câu hỏi một đáp án, kiểm tra xem đáp án người dùng có đúng không
                userAnswers.size == 1 && currentQuestion.correctAnswers.contains(userAnswers.first())
            }
        }

        // Cập nhật số câu trả lời đúng
        if (isCurrentAnswerCorrect) {
            correctAnswersCount++
        }

        // Hiển thị kết quả
        showResult = true
    }

    // Hàm chuyển sang câu hỏi tiếp theo
    fun moveToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            showResult = false
        } else {
            // Đã hoàn thành tất cả câu hỏi
            showCompletionDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top app bar
        TopAppBar(
            title = {
                Text(
                    text = "Làm kiểm tra",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { showExitConfirmation = true }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Thanh tiến trình
        LinearProgressIndicator(
            progress = (currentQuestionIndex + 1).toFloat() / questions.size,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = Color(0xFF42B3FF),
            trackColor = Color(0xFFE0E0E0)
        )

        // Thông tin tiến trình
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Câu ${currentQuestionIndex + 1}/${questions.size}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = "Đúng: $correctAnswersCount",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // Nội dung câu hỏi
        if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Số thứ tự và nội dung câu hỏi
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Số thứ tự
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF42B3FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (currentQuestionIndex + 1).toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Loại câu hỏi
                    Text(
                        text = when (currentQuestion.type) {
                            "SINGLE_CHOICE" -> "Chọn một"
                            "MULTIPLE_CHOICE" -> "Chọn nhiều"
                            "FILL_IN_BLANK" -> "Điền vào chỗ trống"
                            else -> "Chọn một"
                        },
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nội dung câu hỏi
                Text(
                    text = currentQuestion.question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Các lựa chọn hoặc ô nhập liệu
                when (currentQuestion.type) {
                    "FILL_IN_BLANK" -> {
                        // Câu hỏi điền vào chỗ trống
                        var answer by remember(currentQuestionIndex) {
                            mutableStateOf(
                                selectedAnswers[currentQuestionIndex]?.firstOrNull() ?: ""
                            )
                        }

                        OutlinedTextField(
                            value = answer,
                            onValueChange = {
                                answer = it
                                selectedAnswers[currentQuestionIndex] = listOf(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Nhập câu trả lời") },
                            colors = defaultTextFieldColors(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    focusManager.clearFocus()
                                },
                            ),
                            enabled = !showResult
                        )
                    }

                    "MULTIPLE_CHOICE" -> {
                        // Câu hỏi nhiều đáp án
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            currentQuestion.options.forEach { option ->
                                val currentAnswers =
                                    selectedAnswers[currentQuestionIndex] ?: emptyList()
                                val isSelected = currentAnswers.contains(option)
                                val isCorrect = currentQuestion.correctAnswers.contains(option)
                                val showCorrect = showResult && isCorrect

                                OptionItem(
                                    option = option,
                                    isSelected = isSelected,
                                    isCorrect = showCorrect,
                                    onClick = {
                                        if (!showResult) {
                                            val newAnswers = currentAnswers.toMutableList()
                                            if (isSelected) {
                                                newAnswers.remove(option)
                                            } else {
                                                newAnswers.add(option)
                                            }
                                            selectedAnswers = selectedAnswers.toMutableMap().apply {
                                                put(currentQuestionIndex, newAnswers)
                                            }
                                        }
                                    },
                                    isMultipleChoice = true
                                )
                            }
                        }
                    }

                    else -> {
                        // Câu hỏi một đáp án
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            currentQuestion.options.forEach { option ->
                                val currentAnswers =
                                    selectedAnswers[currentQuestionIndex] ?: emptyList()
                                val isSelected = currentAnswers.contains(option)
                                val isCorrect = currentQuestion.correctAnswers.contains(option)
                                val showCorrect = showResult && isCorrect

                                OptionItem(
                                    option = option,
                                    isSelected = isSelected,
                                    isCorrect = showCorrect,
                                    onClick = {
                                        if (!showResult) {
                                            selectedAnswers = selectedAnswers.toMutableMap().apply {
                                                put(currentQuestionIndex, listOf(option))
                                            }
                                        }
                                    },
                                    isMultipleChoice = false
                                )
                            }
                        }
                    }
                }
            }
        }

        // Nút kiểm tra hoặc tiếp tục
        if (!showResult) {
            Button(
                onClick = {
                    checkAnswer()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF42B3FF)
                )
            ) {
                Text(
                    text = "Kiểm tra",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Bottom card hiển thị kết quả
    AnimatedVisibility(
        visible = showResult,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrentAnswerCorrect) Color(0xFFE8F5E9) else Color(
                        0xFFFFEBEE
                    )
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isCurrentAnswerCorrect) "Chính xác!" else "Chưa chính xác!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrentAnswerCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (!isCurrentAnswerCorrect && currentQuestion != null) {
                        Text(
                            text = "Đáp án đúng: ${currentQuestion.correctAnswers.joinToString(", ")}",
                            fontSize = 16.sp,
                            color = Color(0xFF4CAF50)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Hiển thị giải thích nếu có
                    if (currentQuestion?.explanation != null) {
                        Text(
                            text = "Giải thích: ${currentQuestion.explanation}",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Thêm nút Tiếp tục vào trong card
                    Button(
                        onClick = {
                            moveToNextQuestion()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCurrentAnswerCorrect) Color(0xFF4CAF50) else Color(
                                0xFFF44336
                            )
                        )
                    ) {
                        Text(
                            text = "Tiếp tục",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Dialog xác nhận thoát
    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có chắc chắn muốn thoát? Tiến trình làm bài sẽ không được lưu.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitConfirmation = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Thoát", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmation = false }) {
                    Text("Hủy", color = Color.Black)
                }
            }
        )
    }

    // Dialog hoàn thành bài kiểm tra
    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { /* Không cho phép đóng bằng cách nhấn bên ngoài */ },
            title = { Text("Hoàn thành bài kiểm tra") },
            text = {
                Column {
                    Text("Bạn đã hoàn thành bài kiểm tra!")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Kết quả: $correctAnswersCount/${questions.size} câu đúng",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tỷ lệ: ${(correctAnswersCount * 100 / questions.size)}%",
                        color = if (correctAnswersCount * 100 / questions.size >= 80) Color(
                            0xFF4CAF50
                        ) else Color(0xFFF44336)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(ExerciseEvent.FinishQuiz(folderId, quizId))
                        navController.popBackStack()
                    }
                ) {
                    Text("Hoàn thành")
                }
            }
        )
    }
}