package com.example.capyvocab_fe.admin.word.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WordCard(
    word: Word,
    isMultiSelecting: Boolean,
    isSelected: Boolean,
    onPlayAudio: (String) -> Unit,
    onEditClick: (Word) -> Unit,
    onLongClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    cardElevation: Dp = 8.dp
) {
    //animation for checkbox
    val checkboxScale = animateFloatAsState(
        targetValue = if (isMultiSelecting) 1f else 0f,
        animationSpec = tween(300),
        label = "checkboxScale"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isMultiSelecting) {
                        onCheckedChange(!isSelected)
                    }
                },
                onLongClick = {
                    onLongClick()
                }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .scale(checkboxScale.value)
                .width(48.dp * checkboxScale.value),
            contentAlignment = Alignment.Center
        ) {
            if (isMultiSelecting) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onCheckedChange(it) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF99E5F7)),
            elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Content + Pronunciation
                    Text(
                        text = word.content,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "(${word.position}) ${word.pronunciation}",
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Meaning
                    Text(
                        text = word.meaning,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Example
                    Text(
                        text = word.example,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.translateExample,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(onClick = { onPlayAudio(word.audio) }) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Play Audio"
                            )
                        }
                        IconButton(onClick = { onEditClick(word) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Word"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Word Image
                    AsyncImage(
                        model = word.image,
                        contentDescription = "Word Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordCardPreview() {
    CapyVocab_FETheme {
        val sampleWord = Word(
            id = 1,
            content = "apple",
            pronunciation = "/ˈæpl/",
            position = "noun",
            meaning = "a round fruit with red or green skin",
            rank = "2",
            audio = "https://example.com/audio.mp3",
            image = "https://example.com/image.jpg",
            example = "She ate an apple for lunch.",
            translateExample = "Cô ấy đã ăn một quả táo vào bữa trưa."
        )

        WordCard(
            word = sampleWord,
            onPlayAudio = {},
            onEditClick = {},
            onLongClick = {},
            onCheckedChange = {},
            isMultiSelecting = false,
            isSelected = false
        )
    }
}
