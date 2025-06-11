package com.example.capyvocab_fe.user.review.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.admin.word.domain.model.Word

@Composable
fun FillInQuestion(
    word: Word,
    userAnswer: String,
    onAnswer: (String) -> Unit,
    isAnswerShown: Boolean,
    correctAnswer: String
) {
    var input by remember { mutableStateOf(userAnswer) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Nghĩa: ${word.meaning}", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = {
                if (!isAnswerShown) {
                    input = it
                }
            },
            label = { Text("Nhập từ tiếng Anh") },
            enabled = !isAnswerShown,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onAnswer(input) },
            enabled = !isAnswerShown
        ) {
            Text("Trả lời")
        }

        if (isAnswerShown) {
            val isCorrect = input.trim().equals(correctAnswer, ignoreCase = true)
            Text(
                text = if (isCorrect) "Chính xác!" else "Sai! Đáp án: $correctAnswer",
                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
