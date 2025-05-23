package com.example.capyvocab_fe.user.review.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.admin.word.domain.model.Word

@Composable
fun TrueFalseQuestion(
    word: Word,
    displayedMeaning: String,
    onAnswer: (Boolean) -> Unit,
    isAnswerShown: Boolean,
    isCorrectAnswer: Boolean?,
    selectedAnswerBoolean: Boolean?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFDDF7FE), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("Từ: ${word.content}", style = MaterialTheme.typography.titleMedium)
                Text("Có nghĩa là: $displayedMeaning")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(true to "Đúng", false to "Sai").forEach { (value, label) ->
                val isSelected = selectedAnswerBoolean == value
                val isCorrect = isAnswerShown && isSelected && isCorrectAnswer == true
                val isWrong = isAnswerShown && isSelected && isCorrectAnswer == false

                val background = when {
                    isCorrect -> Color(0xFFDFF5E3)
                    isWrong -> Color(0xFFFFEBEB)
                    else -> Color.White
                }

                val borderModifier = if (!isAnswerShown) {
                    Modifier.border(2.dp, Color(0xFFE6EAEB), shape = RoundedCornerShape(12.dp))
                } else {
                    Modifier
                }

                val icon = when {
                    isCorrect -> Icons.Default.Check
                    isWrong -> Icons.Default.Close
                    else -> null
                }

                Button(
                    onClick = { if (!isAnswerShown) onAnswer(value) },
                    colors = ButtonDefaults.buttonColors(containerColor = background, contentColor = Color.Black),
                    shape = RoundedCornerShape(12.dp),
                    modifier = borderModifier
                ) {
                    Text(label)
                    if (icon != null) {
                        Spacer(modifier = Modifier.width(8.dp))
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
}
