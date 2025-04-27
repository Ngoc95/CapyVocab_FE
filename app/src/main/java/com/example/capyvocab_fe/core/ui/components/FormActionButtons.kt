package com.example.capyvocab_fe.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FormActionButtons(
    isEditMode: Boolean,
    onDelete: (() -> Unit)? = null,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isEditMode && onDelete != null) {
            TextButton(onClick = onDelete) {
                Text("Xoá", color = Color(0xFF240000))
            }
        } else {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Row {
            TextButton(onClick = onCancel) {
                Text("Huỷ", color = Color(0xFF240000))
            }
            TextButton(onClick = onSave) {
                Text("Lưu", color = Color(0xFF240000))
            }
        }
    }
}
