package com.example.capyvocab_fe.admin.word.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.presentation.components.WordCard
import com.example.capyvocab_fe.admin.word.presentation.components.WordFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import kotlinx.coroutines.delay

@Composable
fun WordsInTopicScreen(
    topic: Topic,
    viewModel: WordListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var selectedWord by remember { mutableStateOf<Word?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var wordToDelete by remember { mutableStateOf<Word?>(null) }
    var isDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var visibleError by remember { mutableStateOf("") }
    var visibleSuccess by remember { mutableStateOf("") }

    val multiSelectTransition = if (state.isMultiSelecting) {
        remember { mutableStateOf(true) }
    } else {
        remember { mutableStateOf(false) }
    }

    LaunchedEffect(topic.id) {
        viewModel.onEvent(WordEvent.LoadWords(topic))
    }

    //launchEffect to track transition to multi-select mode
    LaunchedEffect(state.isMultiSelecting) {
        multiSelectTransition.value = state.isMultiSelecting
    }
    BackHandler {
        if (state.isMultiSelecting) {
            viewModel.onEvent(WordEvent.CancelMultiSelect)
        } else {
            navController.navigate(Route.HomeScreen.route) {
                popUpTo(Route.HomeScreen.route) {
                    inclusive = false
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
    // Khi errorMessage thay đổi, show snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }
    //Ko đóng dialog khi có lỗi
    LaunchedEffect(state.errorMessage, state.isLoading) {
        if (state.errorMessage.isEmpty() && !state.isLoading) {
            isDialogOpen = false
            selectedWord = null
        }
    }
    LaunchedEffect(state.successMessage) {
        if (state.successMessage.isNotEmpty()) {
            visibleSuccess = state.successMessage
            delay(3000) // hiện 3 giây
            visibleSuccess = "" // ẩn sau 3 giây
        }
    }

    FocusComponent {
        WordsInTopicScreenContent(
            words = state.words,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
            selectedWords = state.words.filter { state.selectedWords.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            onEditWord = { word ->
                selectedWord = word
                isDialogOpen = true
            },
            onAddWord = {
                selectedWord = null
                isDialogOpen = true
            },
            onLoadMore = { viewModel.onEvent(WordEvent.LoadMoreWords(topic)) },
            onWordLongPress = { word ->
                viewModel.onEvent(WordEvent.OnWordLongPress(word.id))
            },
            onWordSelectToggle = { word ->
                viewModel.onEvent(WordEvent.OnWordSelectToggle(word.id))
            }
        )
    }

    if (isDialogOpen) {
        WordFormDialog(
            word = selectedWord,
            errorMessage = visibleError,
            successMessage = visibleSuccess,
            onDismiss = {
                isDialogOpen = false
                selectedWord = null
            },
            onSave = { word, imageUri, audioUri ->
                if (word.id == 0) {
                    viewModel.onEvent(WordEvent.CreateWord(topic.id, word, imageUri, audioUri))
                } else {
                    viewModel.onEvent(WordEvent.UpdateWord(word, imageUri, audioUri))
                }
                isDialogOpen = false
            },
            onDelete = {
                selectedWord?.let {
                    wordToDelete = it
                    isDeleteConfirmDialogOpen = true
                }
                isDialogOpen = false
                selectedWord = null
            }
        )
    }
    //AlertDialog xác nhận trước khi xoá user
    if (isDeleteConfirmDialogOpen && wordToDelete != null) {
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá từ \"${wordToDelete?.content}\" không?",
            onConfirm = {
                viewModel.onEvent(WordEvent.DeleteWord(wordToDelete!!.id))
                isDeleteConfirmDialogOpen = false
                wordToDelete = null
            },
            onDismiss = {
                isDeleteConfirmDialogOpen = false
                wordToDelete = null
            }
        )
    }
}

@Composable
fun WordsInTopicScreenContent(
    words: List<Word>,
    selectedWords: List<Word>,
    isMultiSelectMode: Boolean,
    isLoading: Boolean,
    isEndReached: Boolean,
    onEditWord: (Word) -> Unit,
    onAddWord: () -> Unit,
    onLoadMore: () -> Unit,
    onWordLongPress: (Word) -> Unit,
    onWordSelectToggle: (Word) -> Unit
) {
    val listState = rememberLazyListState()

    // Detect khi cuộn đến gần cuối
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()?.index ?: 0
                val totalItems = listState.layoutInfo.totalItemsCount

                if (lastVisibleItem >= totalItems - 2 && !isLoading && !isEndReached) {
                    onLoadMore()
                }
            }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {
            // Search bar & Add button - hide if in multi-select mode + animation
            AnimatedVisibility(
                visible = !isMultiSelectMode,
                enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                    animationSpec = tween(
                        300
                    ), initialOffsetX = { -it }),
                exit = fadeOut(animationSpec = tween(200)) + slideOutHorizontally(
                    animationSpec = tween(
                        200
                    ), targetOffsetX = { -it })
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp)
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
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                itemsIndexed(words) { index, word ->
                    val isSelected = selectedWords.contains(word)

                    //animation for selection
                    val cardElevation = animateDpAsState(
                        targetValue = if (isSelected && isMultiSelectMode) 12.dp else 8.dp,
                        label = "cardElevation"
                    )
                    val cardScale = animateFloatAsState(
                        targetValue = if (isSelected && isMultiSelectMode) 1.02f else 1f,
                        label = "cardScale"
                    )
                    Box(modifier = Modifier.scale(cardScale.value)) {
                        WordCard(
                            word = word,
                            onEditClick = onEditWord,
                            onLongClick = { onWordLongPress(word) },
                            onCheckedChange = { onWordSelectToggle(word) },
                            isMultiSelecting = isMultiSelectMode,
                            isSelected = isSelected,
                            cardElevation = cardElevation.value
                        )
                    }
                    // Load thêm nếu gần cuối
                    if (index >= words.size - 3 && !isLoading && !isEndReached) {
                        onLoadMore()
                    }
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
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

    WordsInTopicScreenContent(
        words = sampleWords,
        isLoading = false,
        onEditWord = {},
        onAddWord = {},
        onLoadMore = {},
        onWordLongPress = {},
        onWordSelectToggle = {},
        selectedWords = emptyList(),
        isMultiSelectMode = false,
        isEndReached = false,
    )
}


