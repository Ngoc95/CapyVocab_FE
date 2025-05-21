package com.example.capyvocab_fe.user.test.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

/**
 * Composable for adding a new empty flashcard
 */
@Composable
fun EmptyFlashcardCard(
    onAddImage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Definition
            Text(
                text = "Định nghĩa",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Add image button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onAddImage) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Add Image",
                        tint = Color(0xFF42B3FF),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EmptyFlashcardCardPreview() {
    CapyVocab_FETheme {
        EmptyFlashcardCard(
            onAddImage = {}
        )
    }
}