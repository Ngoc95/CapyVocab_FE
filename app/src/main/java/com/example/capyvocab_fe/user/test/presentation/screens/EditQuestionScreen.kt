package com.example.capyvocab_fe.user.test.presentation.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.domain.model.Question
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseViewModel

/**
 * Màn hình chỉnh sửa chi tiết câu hỏi, hỗ trợ cả dạng chọn đáp án (single/multiple) và điền vào chỗ trống.
 * @param quizId ID của bài quiz chứa câu hỏi.
 * @param questionIndex Vị trí câu hỏi trong quiz.
 * @param navController Điều hướng.
 */
@SuppressLint("StateFlowValueCalledInComposition", "MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionScreen(
    quizId: Int,
    questionIndex: Int,
    folderId: Int,
    navController: NavController,
    viewModel: ExerciseViewModel
) {
    val focusManager = LocalFocusManager.current
    // Lấy câu hỏi hiện tại nếu đang chỉnh sửa
    val currentQuiz = viewModel.state.value.currentQuiz
    val currentQuestion = if (questionIndex >= 0 && currentQuiz != null &&
        questionIndex < currentQuiz.question.size) {
        currentQuiz.question[questionIndex]
    } else null
    // Loại câu hỏi: 0 = chọn đáp án, 1 = điền vào chỗ trống
    var questionType by remember {
        mutableIntStateOf(
            if (currentQuestion?.type == "FILL_IN_BLANK") 1 else 0
        )
    }
    // Nếu chọn đáp án: 0 = single, 1 = multiple
    var answerMode by remember {
        mutableIntStateOf(
            if (currentQuestion?.type == "MULTIPLE_CHOICE") 1 else 0
        )
    }

    // Nội dung câu hỏi
    var questionText by remember {
        mutableStateOf(
            TextFieldValue(currentQuestion?.question ?: "")
        )
    }
    // Thêm biến cho phần giải thích
    var explanationText by remember {
        mutableStateOf(
            TextFieldValue(currentQuestion?.explanation ?: "")
        )
    }

    // Danh sách đáp án (cho chọn đáp án)
    var options by remember {
        mutableStateOf(
            if (currentQuestion != null && currentQuestion.options.isNotEmpty()) {
                currentQuestion.options.map { TextFieldValue(it) }.toMutableList()
            } else {
                mutableListOf(TextFieldValue(""), TextFieldValue(""))
            }
        )
    }

    // Đáp án đúng (single: 1 đáp án, multiple: nhiều đáp án)
    var correctAnswers by remember {
        mutableStateOf(
            if (currentQuestion != null && currentQuestion.options.isNotEmpty()) {
                currentQuestion.correctAnswers
                    .map { correct ->
                        currentQuestion.options.indexOfFirst { it == correct }
                    }
                    .filter { it >= 0 }
                    .toMutableSet()
            } else {
                mutableSetOf<Int>()
            }
        )
    }

    // Đáp án đúng cho điền vào chỗ trống
    var fillAnswer by remember {
        mutableStateOf(
            TextFieldValue(
                if (currentQuestion?.type == "FILL_IN_BLANK" &&
                    currentQuestion.correctAnswers.isNotEmpty()) {
                    currentQuestion.correctAnswers.first()
                } else ""
            )
        )
    }

    // Chọn thời gian
    var selectedTime by remember {
        mutableStateOf(currentQuestion?.time ?: "Không giới hạn")
    }
    var showTimePicker by remember { mutableStateOf(false) }
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

    // Trạng thái validate
    var showError by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    // Dialog cảnh báo khi lưu
    var showSaveErrorDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (currentQuestion != null) Text("Chỉnh sửa câu hỏi") else Text("Thêm câu hỏi")},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Chọn thời gian
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showTimePicker = true }
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedTime,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(Icons.Default.AccessTime, contentDescription = "Time Icon")
                }
            }

            // Dropdown chọn loại câu hỏi
            var expandedType by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedType,
                onExpandedChange = { expandedType = !expandedType }
            ) {
                OutlinedTextField(
                    value = if (questionType == 0) "Chọn đáp án" else "Điền vào chỗ trống",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Loại câu hỏi") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedType,
                    onDismissRequest = { expandedType = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Chọn đáp án") },
                        onClick = {
                            questionType = 0
                            expandedType = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Điền vào chỗ trống") },
                        onClick = {
                            questionType = 1
                            expandedType = false
                        }
                    )
                }
            }

            // Nội dung câu hỏi
            OutlinedTextField(
                value = questionText,
                onValueChange = { questionText = it },
                label = { Text("Nội dung câu hỏi") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            // Nếu là chọn đáp án
            if (questionType == 0) {
                // Chọn chế độ single/multiple
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { 
                            // Khi chuyển từ multiple sang single, chỉ giữ đáp án đúng đầu tiên
                            if (answerMode == 1 && correctAnswers.size > 1) {
                                val firstCorrectAnswer = correctAnswers.minOrNull()
                                correctAnswers = if (firstCorrectAnswer != null) {
                                    mutableSetOf(firstCorrectAnswer)
                                } else {
                                    mutableSetOf()
                                }
                            }
                            answerMode = 0 
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (answerMode == 0) Color(0xFF42B3FF) else Color.LightGray
                        )
                    ) { Text("Một đáp án đúng") }
                    Button(
                        onClick = { answerMode = 1 },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (answerMode == 1) Color(0xFF42B3FF) else Color.LightGray
                        )
                    ) { Text("Nhiều đáp án đúng") }
                }

                // Danh sách đáp án
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    options.forEachIndexed { idx, option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    if (correctAnswers.contains(idx)) Color(0xFFE6F7E9) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(4.dp)
                                .fillMaxWidth()
                        ) {
                            // Icon chọn đáp án đúng
                            if (answerMode == 0) {
                                IconButton(
                                    onClick = {
                                        correctAnswers = mutableSetOf(idx) // Gán lại để trigger recomposition
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (correctAnswers.contains(idx)) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked,
                                        contentDescription = "Chọn đáp án đúng",
                                        tint = if (correctAnswers.contains(idx)) Color(0xFF42B3FF) else Color.Gray
                                    )
                                }
                            } else {
                                IconButton(
                                    onClick = {
                                        val newSet = correctAnswers.toMutableSet()
                                        if (newSet.contains(idx)) newSet.remove(idx)
                                        else newSet.add(idx)
                                        correctAnswers = newSet // Gán lại để trigger recomposition
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (correctAnswers.contains(idx)) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank,
                                        contentDescription = "Chọn đáp án đúng",
                                        tint = if (correctAnswers.contains(idx)) Color(0xFF42B3FF) else Color.Gray
                                    )
                                }
                            }

                            // TextField đáp án
                            OutlinedTextField(
                                value = option,
                                onValueChange = {
                                    val newOptions = options.toMutableList()
                                    newOptions[idx] = it
                                    options = newOptions // Gán lại để trigger recomposition
                                },
                                modifier = Modifier.weight(1f),
                                placeholder = { Text("Nhập đáp án") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                    }
                                )
                            )

                            // Nút xóa đáp án (chỉ hiện nếu > 2 đáp án)
                            if (options.size > 2) {
                                IconButton(
                                    onClick = {
                                        val newOptions = options.toMutableList()
                                        newOptions.removeAt(idx)
                                        options = newOptions // Gán lại để trigger recomposition

                                        // Nếu đang là đáp án đúng thì bỏ chọn
                                        val newCorrect = correctAnswers
                                            .mapNotNull { oldIdx ->
                                                when {
                                                    oldIdx < idx -> oldIdx
                                                    oldIdx > idx -> oldIdx - 1
                                                    else -> null
                                                }
                                            }.toMutableSet()
                                        correctAnswers = newCorrect // Gán lại để trigger recomposition
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = "Xóa đáp án",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                    // Thêm đáp án mới
                    TextButton(
                        onClick = {
                            val newOptions = options.toMutableList()
                            newOptions.add(TextFieldValue(""))
                            options = newOptions // Gán lại để trigger recomposition
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Thêm lựa chọn")
                        Text("Thêm lựa chọn")
                    }
                }
            } else {
                // Điền vào chỗ trống
                OutlinedTextField(
                    value = fillAnswer,
                    onValueChange = { fillAnswer = it },
                    label = { Text("Đáp án đúng") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }
                    )
                )
            }

            // trường giải thích
            OutlinedTextField(
                value = explanationText,
                onValueChange = { explanationText = it },
                label = { Text("Giải thích (tùy chọn)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Hiển thị lỗi nếu có
            if (showError) {
                Text(
                    text = errorMsg,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // Nút lưu
            Button(
                onClick = {
                    // Validate trước khi lưu
                    if (questionType == 0) {
                        // Validate đáp án: ít nhất 2 đáp án có chữ, ít nhất 1 đáp án đúng
                        val validOptions = options.map { it.text.trim() }.filter { it.isNotEmpty() }
                        if (validOptions.size < 2) {
                            errorMsg = "Phải có ít nhất 2 đáp án có chữ."
                            showError = true
                            return@Button
                        }
                        if (correctAnswers.isEmpty()) {
                            errorMsg = "Phải chọn ít nhất 1 đáp án đúng."
                            showError = true
                            return@Button
                        }

                        // Tạo câu hỏi mới
                        val validOptionsList = options.map { it.text.trim() }.filter { it.isNotEmpty() }
                        val correctAnswersList = correctAnswers
                            .filter { it < validOptionsList.size }
                            .map { validOptionsList[it] }

                        val newQuestion = Question(
                            question = questionText.text,
                            options = validOptionsList,
                            correctAnswers = correctAnswersList,
                            explanation = explanationText.text.takeIf { it.isNotEmpty() },
                            image = null,
                            time = selectedTime,
                            type = if (answerMode == 0) "SINGLE_CHOICE" else "MULTIPLE_CHOICE"
                        )

                        // Gửi sự kiện cập nhật hoặc thêm mới
                        if (questionIndex >= 0 && currentQuiz != null) {
                            viewModel.onEvent(ExerciseEvent.UpdateQuestionInQuiz(
                                quizId = quizId,
                                questionIndex = questionIndex,
                                question = newQuestion
                            ))
                        } else {
                            viewModel.onEvent(ExerciseEvent.AddQuestionToQuiz(
                                quizId = quizId,
                                question = newQuestion
                            ))
                        }
                        // Gọi lưu folder sau khi thêm mới câu hỏi
                        viewModel.onEvent(ExerciseEvent.SaveFolderWithQuizzes(folderId))

                    } else {
                        // Điền vào chỗ trống: đáp án không được rỗng
                        if (fillAnswer.text.trim().isEmpty()) {
                            errorMsg = "Đáp án không được để trống."
                            showError = true
                            return@Button
                        }

                        // Tạo câu hỏi mới
                        val newQuestion = Question(
                            question = questionText.text,
                            options = emptyList(),
                            correctAnswers = listOf(fillAnswer.text.trim()),
                            explanation = explanationText.text.takeIf { it.isNotEmpty() },
                            image = null,
                            time = selectedTime,
                            type = "FILL_IN_BLANK"
                        )

                        // Gửi sự kiện cập nhật hoặc thêm mới
                        if (questionIndex >= 0 && currentQuiz != null) {
                            viewModel.onEvent(ExerciseEvent.UpdateQuestionInQuiz(
                                quizId = quizId,
                                questionIndex = questionIndex,
                                question = newQuestion
                            ))
                        } else {
                            viewModel.onEvent(ExerciseEvent.AddQuestionToQuiz(
                                quizId = quizId,
                                question = newQuestion
                            ))
                        }
                        // Gọi lưu folder sau khi thêm mới câu hỏi
                        viewModel.onEvent(ExerciseEvent.SaveFolderWithQuizzes(folderId))
                    }
                    showError = false
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lưu câu hỏi")
            }
        }
    }

    if (showTimePicker) {
        ModalBottomSheet(
            onDismissRequest = { showTimePicker = false }
        ) {
            LazyColumn {
                items(timeOptions) { timeOption ->
                    Text(
                        text = timeOption,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedTime = timeOption
                                showTimePicker = false
                            }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }


    // Dialog cảnh báo khi lưu lỗi
    if (showSaveErrorDialog) {
        Dialog(onDismissRequest = { showSaveErrorDialog = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Có lỗi khi lưu câu hỏi!", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showSaveErrorDialog = false }) {
                        Text("Đóng")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditQuestionScreenPreview() {
    CapyVocab_FETheme {
        EditQuestionScreen(
            quizId = 1,
            questionIndex = 0,
            folderId = 1,
            navController = NavController(LocalContext.current),
            viewModel = hiltViewModel()
        )
    }
}