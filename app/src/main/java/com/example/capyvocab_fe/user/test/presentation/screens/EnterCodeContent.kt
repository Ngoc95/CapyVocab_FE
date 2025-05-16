package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors

@Composable
fun EnterCodeContent(modifier: Modifier = Modifier) {
    var codeText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Tieu de
        Text(
            text = "Nhập mã code",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        // Ô nhập mã code
        OutlinedTextField(
            value = codeText,
            onValueChange = { codeText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("022225") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp),
            colors = defaultTextFieldColors(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nút tham gia
        Button(
            onClick = {
                // Xử lý khi người dùng nhấn nút tham gia
                if (codeText.isNotEmpty()) {
                    // TODO: Gọi API để kiểm tra mã code và tham gia bài test
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3) // Màu xanh dương như trong hình
            )
        ) {
            Text(
                text = "Tham gia",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}