package com.example.capyvocab_fe.user.learn.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.admin.word.domain.model.Word

@Composable
fun TypingView(
    word: Word,
    onSubmit: (String) -> Unit,
    showResult: Boolean,
) {
    var answer by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nghe và điền từ:",
            style = MaterialTheme.typography.titleMedium
        )

        PronunciationPlayer(audioUrl = word.audio ?: "", wordId = word.id)

        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Điền câu trả lời") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
                onSubmit(answer)
            }),
            enabled = !showResult
        )

        Button(
            onClick = {
                focusManager.clearFocus()
                onSubmit(answer)
            },
            enabled = answer.isNotBlank() && !showResult
        ) {
            Text("Kiểm tra")
        }

    }
}

