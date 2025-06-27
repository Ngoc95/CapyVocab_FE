package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.user.test.domain.model.Question
import com.example.capyvocab_fe.user.test.domain.model.Quiz
import com.example.capyvocab_fe.user.test.presentation.screens.components.QuestionItem
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseState

/**
 * Screen for taking or editing a quiz
 * @param navController Navigation controller for navigating between screens
 * @param quizId ID of the quiz
 * @param state Current UI state
 * @param onEvent Event handler for user actions
 * @param isEditing Whether the user is editing the quiz
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    quizId: Int,
    folderId: Int,
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
) {
    // Trạng thái chuyển đổi giữa chế độ thường và chỉnh sửa
    var editingMode by rememberSaveable { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedQuestionIndex by remember { mutableIntStateOf(-1) }

    val quiz = state.currentQuiz
    var showAnswers by remember { mutableStateOf(false) }

    // Hiện chọn list thời gian
    var showTimePicker by remember { mutableStateOf(false) }

    // Hiển thị dialog xác nhận xóa
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    
    // Kiểm tra xem có câu hỏi nào không
    val hasQuestions = quiz?.question?.isNotEmpty() == true
    // Kiểm tra người dùng hiện tại có phải là người tạo không
    val isCreator = state.currentFolder?.createdBy?.id == state.currentUser?.id
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top app bar
        TopAppBar(
            title = {
                Text(
                    text = if (editingMode) "Chỉnh sửa" else "Quiz",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { 
                    if (editingMode) {
                        editingMode = false
                    } else {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                if (!editingMode && isCreator) {
                    // Nút chuyển sang chế độ chỉnh sửa
                    Text(
                        text = "Chỉnh sửa",
                        color = Color(0xFF42B3FF),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                editingMode = true
                                showAnswers = true
                            }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Quiz info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tổng câu hỏi: ${quiz?.question?.size ?: 0}",
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            // Show answers toggle - chỉ hiển thị khi có câu hỏi và không ở chế độ chỉnh sửa
            if (hasQuestions && isCreator && !editingMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hiện đáp án",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Switch(
                        checked = showAnswers,
                        onCheckedChange = { showAnswers = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF42B3FF),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.LightGray
                        )
                    )
                }
            }
        }

        // Nội dung chính - danh sách câu hỏi hoặc thông báo trống
        if (hasQuestions) {
            // Questions list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val questions = quiz?.question

                if (questions != null) {
                    items(questions.size) { index ->
                        val question = questions[index]
                        Box {
                            QuestionItem(
                                questionNumber = index + 1,
                                question = question,
                                showAnswers = (showAnswers && isCreator) || (editingMode && isCreator),
                                selectedAnswers = if ((showAnswers && isCreator) || (editingMode && isCreator))
                                    question.correctAnswers
                                else
                                    state.selectedAnswers[index] ?: emptyList(),
                                onAnswerSelected = {},
                                isEditing = editingMode,
                                onMoreClick = {
                                    selectedQuestionIndex = index
                                    showBottomSheet = true
                                }
                            )
                        }
                    }
                }

                // Add floating action button space
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        } else {
            // Hiển thị thông báo khi không có câu hỏi
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Chưa có câu hỏi nào",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    if (editingMode) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nhấn nút + để thêm câu hỏi mới",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Bottom button - chỉ hiển thị khi có câu hỏi hoặc không ở chế độ chỉnh sửa
        if (hasQuestions && !editingMode) {
            Button(
                onClick = {
                    // Chuyển đến màn hình DoQuizScreen
                    navController.navigate("${Route.DoQuizScreen.route}/${quizId}/${folderId}")
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
                    text = "Làm kiểm tra",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    // Floating action button for adding new question (only in edit mode)
    if (editingMode && isCreator) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    // Chuyển đến màn hình thêm câu hỏi mới
                    navController.navigate("${Route.EditQuestionScreen.route}/${quizId}/-1/${folderId}")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp),
                containerColor = Color(0xFF42B3FF),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Question"
                )
            }
        }
    }

    //Hiện bottomsheet list thời gian
    val timeOptions = listOf(
        "Không giới hạn",
        "5 giây",
        "10 giây",
        "20 giây",
        "30 giây",
        "45 giây",
        "1 phút",
        "1.5 phút",
        "2 phút",
        "3 phút",
        "5 phút",
        "10 phút",
        "15 phút",
        "20 phút",
    )

    if (showTimePicker) {
        ModalBottomSheet(onDismissRequest = { showTimePicker = false }) {
            LazyColumn {
                items(timeOptions) { timeOption ->
                    Text(
                        text = timeOption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showTimePicker = false
                                if (selectedQuestionIndex >= 0 && quiz != null) {
                                    val updatedQuestion = quiz.question[selectedQuestionIndex].copy(time = timeOption)

                                    onEvent(
                                        ExerciseEvent.UpdateQuestionInQuiz(
                                            quizId = quizId,
                                            questionIndex = selectedQuestionIndex,
                                            question = updatedQuestion
                                        )
                                    )
                                    onEvent(ExerciseEvent.SaveFolderWithQuizzes(folderId))
                                }
                            }
                            .padding(12.dp)
                    )
                }
            }
        }
    }

    // Dialog xác nhận xóa câu hỏi
    if (showDeleteConfirmation && isCreator) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa câu hỏi này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        // Xóa câu hỏi
                        onEvent(ExerciseEvent.DeleteQuestionFromQuiz(
                            quizId = quizId,
                            questionIndex = selectedQuestionIndex
                        ))
                        onEvent(ExerciseEvent.SaveFolderWithQuizzes(folderId))
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
    
    // Bottom sheet cho từng câu hỏi khi ấn 3 chấm
    if (showBottomSheet && isCreator) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Chỉnh sửa câu hỏi",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            // Chuyển sang màn hình chỉnh sửa câu hỏi
                            navController.navigate("${Route.EditQuestionScreen.route}/${quizId}/${selectedQuestionIndex}/${folderId}")
                        }
                        .padding(vertical = 12.dp)
                )
                Text(
                    text = "Sao chép câu hỏi",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            onEvent(ExerciseEvent.DuplicateQuestion(
                                quizId = quizId,
                                questionIndex = selectedQuestionIndex
                            ))
                            onEvent(ExerciseEvent.SaveFolderWithQuizzes(folderId))
                        }
                        .padding(vertical = 12.dp)
                )
                Text(
                    text = "Thay đổi thời gian",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            showTimePicker = true
                        }
                        .padding(vertical = 12.dp)
                )

                Text(
                    text = "Xóa câu hỏi",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showBottomSheet = false
                            showDeleteConfirmation = true
                        }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun QuizScreenPreview() {
    val sampleQuiz = Quiz(
        id = 1,
        title = "Sample Quiz",
        question = listOf(
            Question(
                question = "What is the capital of France?",
                options = listOf("Paris", "London", "Berlin", "Madrid"),
                correctAnswers = listOf("Paris"),
                explanation = "Paris is the capital and most populous city of France.",
                type = "SINGLE_CHOICE"
            ),
            Question(
                question = "Which of these are programming languages?",
                options = listOf("Java", "Python", "HTML", "CSS"),
                correctAnswers = listOf("Java", "Python"),
                explanation = "Java and Python are programming languages, while HTML and CSS are markup languages.",
                type = "MULTIPLE_CHOICE"
            )
        )
    )

    val sampleState = ExerciseState(
        currentQuiz = sampleQuiz,
        selectedAnswers = mapOf(
            0 to listOf("Paris"),
            1 to listOf("Java")
        )
    )

    MaterialTheme {
        QuizScreen(
            navController = rememberNavController(),
            quizId = 1,
            folderId = 1,
            state = sampleState,
            onEvent = {}
        )
    }
}