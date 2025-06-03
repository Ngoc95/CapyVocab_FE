package com.example.capyvocab_fe.user.test.presentation.screens.screen_contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.data.remote.model.CreateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.Folder

@Composable
fun CreateTestContent(
    modifier: Modifier = Modifier,
    isCreating: Boolean = false,
    onCreateFolder: (CreateFolderRequest, (Folder) -> Unit, (String) -> Unit) -> Unit,
    onFolderCreated: (Folder) -> Unit = {}
) {
    var testTitle by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("0") }
    var isPublic by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Tạo folder mới",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = testTitle,
            onValueChange = { testTitle = it },
            label = { Text("Tiêu đề folder") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = defaultTextFieldColors(),
            isError = errorMessage != null
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Giá tiền") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            colors = defaultTextFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            isError = errorMessage != null
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Switch(
                checked = isPublic,
                onCheckedChange = { isPublic = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Công khai folder")
        }

        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (testTitle.isBlank()) {
                    errorMessage = "Vui lòng nhập tiêu đề folder"
                    return@Button
                }
                errorMessage = null
                onCreateFolder(
                    CreateFolderRequest(testTitle, price.toDouble(), isPublic),
                    { folder ->
                        testTitle = ""
                        price = "0"
                        isPublic = false
                        onFolderCreated(folder)
                    },
                    { error ->
                        errorMessage = error
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42B3FF)
            ),
            enabled = !isCreating
        ) {
            Text(
                text = if (isCreating) "Đang tạo..." else "Tạo folder",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
private fun CreateTestContentPreview() {
    CapyVocab_FETheme {
        CreateTestContent(isCreating = false,
            onCreateFolder = { _, _, _ -> })
    }
}