package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Folder

@Composable
fun TestSettingScreen(
    folder: Folder,
    onSaveClick: (UpdateFolderRequest) -> Unit,
    onBackClick: () -> Unit
) {
    var folderName by remember { mutableStateOf(folder.name) }
    var isPublic by remember { mutableStateOf(folder.isPublic) }
    var priceText by remember { mutableStateOf(folder.price.toString()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
            }
            Text("Cài đặt thư mục", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tên thư mục
        OutlinedTextField(
            value = folderName,
            onValueChange = { folderName = it },
            label = { Text("Tên thư mục") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quyền xem
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Ai có thể xem", fontSize = 16.sp)
            Switch(
                checked = isPublic,
                onCheckedChange = { isPublic = it }
            )
        }
        Text(
            text = if (isPublic) "Mọi người có thể xem" else "Chỉ mình tôi",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Giá
        OutlinedTextField(
            value = priceText,
            onValueChange = {
                if (it.all { c -> c.isDigit() }) {
                    priceText = it
                }
            },
            label = { Text("Giá (đồng)") },
            placeholder = { Text("Nhập 0 nếu miễn phí") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút lưu
        Button(
            onClick = {
                val newPrice = priceText.toDoubleOrNull() ?: 0.0
                val updated = UpdateFolderRequest(
                    name = folderName,
                    isPublic = isPublic,
                    price = newPrice
                )
                onSaveClick(updated)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Lưu thay đổi")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FolderSettingScreenPreview() {
    CapyVocab_FETheme {
        TestSettingScreen(
            folder = Folder(
                id = 1,
                name = "Test Folder",
                code = "test123",
                price = 0.0,
                isPublic = true,
                createdBy = null,
                voteCount = 0,
                commentCount = 0,
                totalAttemptCount = 10,
                isAlreadyVote = false,
                quizzes = null,
                flashCards = null,
                comments = null
            ),
            onSaveClick = {},
            onBackClick = {}
        )
    }
}