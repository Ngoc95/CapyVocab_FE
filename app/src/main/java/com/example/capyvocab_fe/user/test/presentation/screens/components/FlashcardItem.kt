package com.example.capyvocab_fe.user.test.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.domain.model.FlashCard

/**
 * Composable for displaying an existing flashcard
 */
@Composable
fun FlashcardItem(
    flashcard: FlashCard,
    isEditing: Boolean,
    searchQuery: String = "",
    onUpdate: (FlashCard) -> Unit,
    onDelete: () -> Unit,
    onSelectFrontImage: () -> Unit,
    onSelectBackImage: () -> Unit
) {
    var frontContent by remember { mutableStateOf(flashcard.frontContent.orEmpty()) }
    var backContent by remember { mutableStateOf(flashcard.backContent.orEmpty()) }

    LaunchedEffect(flashcard.hashCode()) {
        frontContent = flashcard.frontContent.orEmpty()
        backContent = flashcard.backContent.orEmpty()
    }

    LaunchedEffect(frontContent, backContent) {
        onUpdate(flashcard.copy(frontContent = frontContent, backContent = backContent))
    }

    val showFrontError = isEditing && frontContent.isBlank()
    val showBackError = isEditing && backContent.isBlank()

    val highlightColor = Color(0xFFFFA000)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Mặt trước
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Từ vựng", fontSize = 12.sp, color = Color.Gray)

                    if (isEditing) {
                        OutlinedTextField(
                            value = frontContent,
                            onValueChange = { frontContent = it },
                            colors = defaultTextFieldColors(),
                            singleLine = true,
                            enabled = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (showFrontError) {
                            Text(
                                text = "Không được để trống từ vựng",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = getHighlightedText(frontContent, searchQuery, highlightColor),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (isEditing || flashcard.frontImage != "N/A") {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(enabled = isEditing) { onSelectFrontImage() }
                    ) {
                        AsyncImage(
                            model = flashcard.frontImage ?: R.drawable.add_avt,
                            contentDescription = "Front Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.add_avt),
                            error = painterResource(id = R.drawable.add_avt)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            // Mặt sau
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Định nghĩa", fontSize = 12.sp, color = Color.Gray)

                    if (isEditing) {
                        OutlinedTextField(
                            value = backContent,
                            onValueChange = { backContent = it },
                            colors = defaultTextFieldColors(),
                            singleLine = false,
                            enabled = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (showBackError) {
                            Text(
                                text = "Không được để trống định nghĩa",
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    } else {
                        Text(
                            text = getHighlightedText(backContent, searchQuery, highlightColor),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (isEditing || flashcard.backImage != "N/A") {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .clickable(enabled = isEditing) { onSelectBackImage() }
                            .padding(top = 4.dp)
                    ) {
                        AsyncImage(
                            model = flashcard.backImage ?: R.drawable.add_avt,
                            contentDescription = "Back Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.add_avt),
                            error = painterResource(id = R.drawable.add_avt)
                        )
                    }
                }
            }

            if (isEditing) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun getHighlightedText(text: String, keyword: String, highlightColor: Color): AnnotatedString {
    val lowerText = text.lowercase()
    val lowerKeyword = keyword.trim().lowercase()
    val builder = AnnotatedString.Builder()

    if (lowerKeyword.isEmpty()) return AnnotatedString(text)

    var currentIndex = 0
    while (currentIndex < text.length) {
        val matchIndex = lowerText.indexOf(lowerKeyword, currentIndex)
        if (matchIndex == -1) {
            builder.append(text.substring(currentIndex))
            break
        }
        // Append before match
        builder.append(text.substring(currentIndex, matchIndex))
        // Highlight match
        builder.withStyle(SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold)) {
            builder.append(text.substring(matchIndex, matchIndex + lowerKeyword.length))
        }
        currentIndex = matchIndex + lowerKeyword.length
    }

    return builder.toAnnotatedString()
}

@Preview
@Composable
private fun FlashcardItemPreview() {
    CapyVocab_FETheme {
        FlashcardItem(
            flashcard = FlashCard(
                frontContent = "Hello",
                backContent = "Xin chào",
                frontImage = null,
                backImage = null
            ),
            isEditing = true,
            onUpdate = {},
            onDelete = {},
            onSelectFrontImage = {},
            onSelectBackImage = {}
        )
    }
}