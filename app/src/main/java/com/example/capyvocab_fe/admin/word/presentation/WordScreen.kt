package com.example.capyvocab_fe.admin.word.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.presentation.components.WordCard
import com.example.capyvocab_fe.admin.word.presentation.components.WordFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.core.util.components.FocusComponent

@Composable
fun WordScreen(
    topic: Topic,
    viewModel: WordListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedWord by remember { mutableStateOf<Word?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(topic.id) {
        viewModel.onEvent(WordEvent.LoadWords(topic.id))
    }

    FocusComponent {
        WordScreenContent (
            words = state.words,
            topicTitle = topic.title,
            onPlayAudio = { audioUrl ->
                // TODO: Play audio
            },
            onEditWord = { word ->
                selectedWord = word
                isDialogOpen = true
            },
            onAddWord = {
                selectedWord = null
                isDialogOpen = true
            }
        )
    }

    if (isDialogOpen) {
        WordFormDialog(
            word = selectedWord,
            errorMessage = "",
            onDismiss = {
                isDialogOpen = false
                selectedWord = null
            },
            onSave = { word, uri ->
                if (word.id == 0) {
                    viewModel.onEvent(WordEvent.CreateWord(word))
                } else {
                    viewModel.onEvent(WordEvent.UpdateWord(word))
                }
                isDialogOpen = false
            },
            onDelete = {
                selectedWord?.let { word ->
                    viewModel.onEvent(WordEvent.DeleteWord(word.id))
                }
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun WordScreenContent(
    words: List<Word>,
    topicTitle: String,
    onPlayAudio: (String) -> Unit,
    onEditWord: (Word) -> Unit,
    onAddWord: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopBarTitle(topicTitle, 25.sp)

        // Search bar & Add button
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var searchQuery by remember { mutableStateOf("") }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Tìm từ vựng") },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .size(width = 39.dp, height = 36.dp)
                            .background(
                                color = Color(0xFF00D9FF),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                colors = defaultTextFieldColors(),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null,
                modifier = Modifier
                    .size(55.dp)
                    .clickable(onClick = onAddWord)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            items(words) { word ->
                WordCard(
                    word = word,
                    onPlayAudio = onPlayAudio,
                    onEditClick = onEditWord,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordListScreenContentPreview() {
    val sampleWords = listOf(
        Word(
            id = 1,
            content = "apple",
            pronunciation = "/ˈæpl/",
            position = "noun",
            meaning = "a round fruit with red or green skin",
            audio = "https://example.com/audio1.mp3",
            image = "https://example.com/image1.jpg",
            example = "She ate an apple for lunch.",
            translateExample = "Cô ấy đã ăn một quả táo vào bữa trưa."
        ),
        Word(
            id = 2,
            content = "run",
            pronunciation = "/rʌn/",
            position = "verb",
            meaning = "to move quickly on foot",
            audio = "https://example.com/audio2.mp3",
            image = "https://example.com/image2.jpg",
            example = "He runs every morning.",
            translateExample = "Anh ấy chạy mỗi sáng."
        )
    )

    WordScreenContent(
        words = sampleWords,
        topicTitle = "Cuộc sống hằng ngày",
        onPlayAudio = {},
        onEditWord = {},
        onAddWord = {}
    )
}


