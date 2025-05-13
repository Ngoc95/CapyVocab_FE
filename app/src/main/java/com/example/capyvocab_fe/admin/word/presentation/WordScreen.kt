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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.presentation.components.WordCard
import com.example.capyvocab_fe.admin.word.presentation.components.WordFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.ui.components.RippleOverlay
import com.example.capyvocab_fe.core.util.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import kotlinx.coroutines.delay

@Composable
fun WordScreen(
    viewModel: WordListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var selectedWord by remember { mutableStateOf<Word?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var wordToDelete by remember { mutableStateOf<Word?>(null) }
    var isDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var isMultiDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var visibleError by remember { mutableStateOf("") }

    val multiSelectTransition = if (state.isMultiSelecting) {
        remember { mutableStateOf(true) }
    } else {
        remember { mutableStateOf(false) }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(WordEvent.LoadAllWords)
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

    FocusComponent {
        WordScreenContent(
            words = state.words,
            selectedWords = state.words.filter { state.selectedWords.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
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
            },
            onLoadMore = { viewModel.onEvent(WordEvent.LoadMoreAllWords) },
            onCancelMultiSelect = { viewModel.onEvent(WordEvent.CancelMultiSelect) },
            onDeleteSelectedWords = {
                isMultiDeleteConfirmDialogOpen = true
            },
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
            errorMessage = "",
            onDismiss = {
                isDialogOpen = false
                selectedWord = null
            },
            onSave = { word, uri ->
                if (word.id == 0) {
                    viewModel.onEvent(WordEvent.CreateWord(1, word))
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

    //multi-user delete confirmation dialog
    if (isMultiDeleteConfirmDialogOpen) {
        val selectedCount = state.selectedWords.size
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá $selectedCount từ đã chọn không?",
            onConfirm = {
                viewModel.onEvent(WordEvent.OnDeleteSelectedWords)
                isMultiDeleteConfirmDialogOpen = false
            },
            onDismiss = {
                isMultiDeleteConfirmDialogOpen = false
            }
        )
    }

    //overlay when entering multi-select mode
    if (multiSelectTransition.value && state.isMultiSelecting) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp)
        ) {
            RippleOverlay(
                onFinished = {/* optional callback */ }
            )
        }
    }
}

@Composable
fun WordScreenContent(
    words: List<Word>,
    selectedWords: List<Word>,
    isMultiSelectMode: Boolean,
    isLoading: Boolean,
    isEndReached: Boolean,
    onPlayAudio: (String) -> Unit,
    onEditWord: (Word) -> Unit,
    onAddWord: () -> Unit,
    onLoadMore: () -> Unit,
    onCancelMultiSelect: () -> Unit,
    onDeleteSelectedWords: () -> Unit,
    onWordLongPress: (Word) -> Unit,
    onWordSelectToggle: (Word) -> Unit
) {
    val listState = rememberLazyListState()

    //animation values
    val topBarScale = animateFloatAsState(
        targetValue = if (isMultiSelectMode) 1.05f else 1f,
        animationSpec = tween(300),
        label = "topBarScale"
    )

    val topBarElevation = animateDpAsState(
        targetValue = if (isMultiSelectMode) 8.dp else 0.dp,
        animationSpec = tween(300),
        label = "topBarElevation"
    )

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
            // Top bar + animation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 4.dp, start = 12.dp, end = 12.dp)
                    .scale(topBarScale.value)
                    .shadow(topBarElevation.value, RoundedCornerShape(16.dp))
                    .background(
                        color = if (isMultiSelectMode) Color(0xFF8FD9FF) else Color.Transparent
                    )
                    .padding(vertical = 8.dp)
            ) {
                if (isMultiSelectMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        //left side with back button and selection count
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInHorizontally(),
                                exit = fadeOut() + slideOutHorizontally()
                            ) {
                                IconButton(onClick = { onCancelMultiSelect() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "Cancel")
                                }
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Đã chọn ${selectedWords.size}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                        }

                        //right side with delete button
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + slideInHorizontally(initialOffsetX = { it / 2 }),
                            exit = fadeOut() + slideOutHorizontally(targetOffsetX = { it / 2 })
                        ) {
                            IconButton(onClick = { onDeleteSelectedWords() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }

                }
            }

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
                            onPlayAudio = onPlayAudio,
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