package com.example.capyvocab_fe.user.review.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.admin.word.domain.model.Word

@Composable
fun ChooseMeaningQuestion(
    word: Word,
    options: List<String>,
    onAnswer: (String) -> Unit,
    isAnswerShown: Boolean,
    correctAnswer: String?,
    selectedAnswer: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDDF7FE), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("Từ: ${word.content}", style = MaterialTheme.typography.titleMedium)
                Text("Có nghĩa là:")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        options.forEach { option ->
            val isCorrect = isAnswerShown && option == correctAnswer
            val isWrong = isAnswerShown && option == selectedAnswer && option != correctAnswer

            val backgroundColor = when {
                isCorrect -> Color(0xFFDFF5E3)
                isWrong -> Color(0xFFFFEBEB)
                selectedAnswer == option -> Color(0xFFE0E0E0)
                else -> Color.White
            }

            val borderColor = when {
                isCorrect -> Color(0xFF4CAF50)
                isWrong -> Color(0xFFF44336)
                else -> Color(0xFFE6EAEB)
            }

            val icon = when {
                isCorrect -> Icons.Default.Check
                isWrong -> Icons.Default.Close
                else -> null
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    .clickable(enabled = !isAnswerShown) {
                        onAnswer(option)
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = option,
                    modifier = Modifier.weight(1f),
                    color = Color.Black
                )

                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
                    )
                }
            }
        }
    }
}
