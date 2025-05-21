package com.example.capyvocab_fe.user.test.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.domain.model.FlashCard

/**
 * Composable for displaying an existing flashcard
 */
@Composable
fun FlashcardItem(
    flashcard: FlashCard,
    isEditing: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEditing) { onEdit() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Term
            Text(
                text = "Từ vựng",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Text(
                text = flashcard.frontContent,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider()

            // Definition
            Text(
                text = "Định nghĩa",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = flashcard.backContent,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Front Image if available
            flashcard.frontImage?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Front Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.add_avt),
                    error = painterResource(id = R.drawable.add_avt)
                )
            }

            // Back Image if available
            flashcard.backImage?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Back Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.add_avt),
                    error = painterResource(id = R.drawable.add_avt)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FlashcardItemPreview() {
    CapyVocab_FETheme {
        FlashcardItem(
            flashcard = FlashCard(
                id = 1,
                frontContent = "Hello",
                backContent = "Xin chào",
                frontImage = null,
                backImage = null
            ),
            isEditing = true,
            onEdit = {},
            onDelete = {}
        )
    }
}