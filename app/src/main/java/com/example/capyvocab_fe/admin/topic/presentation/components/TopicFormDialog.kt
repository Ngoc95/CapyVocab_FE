package com.example.capyvocab_fe.admin.topic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.admin.topic.domain.model.Topic

@Composable
fun TopicFormDialog(
    topic: Topic?,
    errorMessage: String,
    onDismiss: () -> Unit,
    onSave: (Topic) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(topic?.title.orEmpty()) }
    var description by remember { mutableStateOf(topic?.description.orEmpty()) }
    var thumbnail by remember { mutableStateOf(topic?.thumbnail.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (topic == null) "Thêm chủ đề" else "Chỉnh sửa chủ đề") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên chủ đề") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Mô tả (không bắt buộc)") },
                    singleLine = false,
                    maxLines = 3
                )
                OutlinedTextField(
                    value = thumbnail,
                    onValueChange = { thumbnail = it },
                    label = { Text("URL Ảnh đại diện") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        Topic(
                            id = topic?.id ?: 0,
                            title = title,
                            description = description,
                            type = "Free",
                            thumbnail = thumbnail,
                            alreadyLearned = topic?.alreadyLearned ?: false
//                            deletedAt = null,
//                            createdAt = "",
//                            updatedAt = "",
//                            displayOrder = 1
                        )
                    )
                }
            ) {
                Text("Lưu")
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (topic != null) {
                    TextButton(onClick = onDelete) {
                        Text("Xóa", color = Color.Red)
                    }
                }
                TextButton(onClick = onDismiss) {
                    Text("Hủy")
                }
            }
        }
    )
}
