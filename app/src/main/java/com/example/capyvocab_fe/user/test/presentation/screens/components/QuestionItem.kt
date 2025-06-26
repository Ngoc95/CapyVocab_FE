package com.example.capyvocab_fe.user.test.presentation.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.user.test.domain.model.Question

/**
 * Displays a single question item with options
 */
@Composable
fun QuestionItem(
    questionNumber: Int,
    question: Question,
    showAnswers: Boolean,
    selectedAnswers: List<String>,
    onAnswerSelected: (String) -> Unit,
    isEditing: Boolean = false,
    onMoreClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Question header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Question number
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFF42B3FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = questionNumber.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Question type
                Text(
                    text = when (question.type) {
                        "SINGLE_CHOICE" -> "Chọn một"
                        "MULTIPLE_CHOICE" -> "Chọn nhiều"
                        "FILL_IN_BLANK" -> "Điền vào chỗ trống"
                        else -> "Chọn một"
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.weight(1f))
                // Question time
                if(question.time != "Không giới hạn") {
                    question.time?.let {
                        Text(text = it, fontSize = 14.sp, color = Color.Gray)
                    }
                }

                if (isEditing && onMoreClick != null) {
                    IconButton(onClick = onMoreClick) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Question text
            Text(
                text = question.question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hiển thị nội dung câu hỏi dựa vào loại
            when (question.type) {
                "FILL_IN_BLANK" -> {
                    // Hiển thị ô nhập cho câu hỏi điền vào chỗ trống
                    var answer by remember { mutableStateOf(TextFieldValue(selectedAnswers.firstOrNull() ?: "")) }

                    OutlinedTextField(
                        value = answer,
                        onValueChange = {
                            answer = it
                            onAnswerSelected(it.text)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Nhập câu trả lời") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF42B3FF),
                            unfocusedBorderColor = Color.Gray
                        ),
                        enabled = false
                    )

                    // Hiển thị đáp án đúng nếu showAnswers = true
                    if (showAnswers) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Đáp án đúng: ${question.correctAnswers.joinToString(", ")}",
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                "MULTIPLE_CHOICE" -> {
                    // Hiển thị các lựa chọn cho câu hỏi nhiều đáp án
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        question.options.forEachIndexed { index, option ->
                            val isSelected = selectedAnswers.contains(option)
                            val isCorrect = question.correctAnswers.contains(option)
                            val showCorrect = showAnswers && isCorrect

                            OptionItem(
                                option = option,
                                isSelected = isSelected,
                                isCorrect = showCorrect,
                                onClick = { onAnswerSelected(option) },
                                isMultipleChoice = true
                            )
                        }
                    }
                }
                else -> {
                    // Hiển thị các lựa chọn cho câu hỏi một đáp án (mặc định)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        question.options.forEachIndexed { index, option ->
                            val isSelected = selectedAnswers.contains(option)
                            val isCorrect = question.correctAnswers.contains(option)
                            val showCorrect = showAnswers && isCorrect

                            OptionItem(
                                option = option,
                                isSelected = isSelected,
                                isCorrect = showCorrect,
                                onClick = { onAnswerSelected(option) },
                                isMultipleChoice = false
                            )
                        }
                    }
                }
            }

            // Explanation (only shown when answers are visible)
            if (showAnswers && question.explanation != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF0F8FF), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Giải thích:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = question.explanation,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}