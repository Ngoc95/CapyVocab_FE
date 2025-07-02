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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Folder
import com.example.capyvocab_fe.util.PriceUtils.formatPrice
import com.example.capyvocab_fe.util.PriceUtils.unformatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestSettingScreen(
    folder: Folder,
    onSaveClick: (UpdateFolderRequest) -> Unit,
    onDelete: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    var folderName by remember { mutableStateOf(folder.name) }
    var isPublic by remember { mutableStateOf(folder.isPublic) }
    var priceText by remember { mutableStateOf(TextFieldValue(formatPrice(folder.price))) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Header
        TopAppBar(
            title = {
                Text(
                    text = "Cài đặt thư mục",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                }
            },
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tên thư mục
        OutlinedTextField(
            value = folderName,
            onValueChange = { folderName = it },
            label = { Text("Tên thư mục") },
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Quyền xem
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Giá
        OutlinedTextField(
            value = priceText,
            onValueChange = { newValue ->
                val oldUnformatted = priceText.text.replace(",", "")
                val newUnformatted = newValue.text.replace(",", "")
                val formatted = if (newUnformatted.isNotEmpty()) formatPrice(newUnformatted.toDouble()) else ""
                // Calculate new cursor position
                val diff = formatted.length - newValue.text.length
                val newCursor = (newValue.selection.end + diff).coerceIn(0, formatted.length)
                priceText = TextFieldValue(
                    text = formatted,
                    selection = androidx.compose.ui.text.TextRange(newCursor)
                )
            },
            label = { Text("Giá (đồng)") },
            placeholder = { Text("Nhập 0 nếu miễn phí") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút lưu
        Button(
            onClick = {
                val newPrice = unformatPrice(priceText.text)
                val updated = UpdateFolderRequest(
                    name = folderName,
                    isPublic = isPublic,
                    price = newPrice
                )
                onSaveClick(updated)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("Lưu thay đổi")
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.small2))
        // Nút xoá thư mục
        OutlinedButton(
            onClick = { showDeleteConfirmation = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
        ) {
            Text("Xoá thư mục")
        }
    }
    // Xác nhận xoá
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Xoá thư mục") },
            text = { Text("Bạn có chắc chắn muốn xoá thư mục này?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(folder.id)
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Xoá", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Huỷ")
                }
            }
        )
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
            onBackClick = {},
            onDelete = { }
        )
    }
}