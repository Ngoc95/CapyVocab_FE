package com.example.capyvocab_fe.user.test.presentation.screens.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

/**
 * Composable for adding a new flashcard
 */
@Composable
fun NewFlashcardCard(
    selectedImageUri: Uri?, // <- ảnh đã chọn
    onAddImage: () -> Unit
) {
    var frontContent by remember { mutableStateOf("") }
    var backContent by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Từ vựng", fontSize = 12.sp, color = Color.Gray)
                OutlinedTextField(
                    value = frontContent,
                    onValueChange = { frontContent = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors(),
                    singleLine = true
                )

                Text(
                    text = "Định nghĩa",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
                OutlinedTextField(
                    value = backContent,
                    onValueChange = { backContent = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors(),
                    singleLine = true
                )
            }

            Column(
                modifier = Modifier
                    .width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Ảnh đã chọn (nếu có)
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = selectedImageUri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    IconButton(onClick = onAddImage) {
                        Icon(
                            painter = painterResource(id = R.drawable.add_avt),
                            contentDescription = "Add Image",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Unspecified
                        )
                    }
                }

                Text(
                    text = "Add Image",
                    fontSize = 12.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun NewFlashcardCardPreview() {
    CapyVocab_FETheme {
        NewFlashcardCard(
            selectedImageUri = null,
            onAddImage = {}
        )
    }
}
