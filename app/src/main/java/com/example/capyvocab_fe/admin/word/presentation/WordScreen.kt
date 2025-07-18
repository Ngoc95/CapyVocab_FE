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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.admin.word.presentation.components.WordCard
import com.example.capyvocab_fe.admin.word.presentation.components.WordFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.core.ui.components.SnackbarType
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.dimens
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

    var visibleError by remember { mutableStateOf("") }
    var visibleSuccess by remember { mutableStateOf("") }

    val multiSelectTransition = if (state.isMultiSelecting) {
        remember { mutableStateOf(true) }
    } else {
        remember { mutableStateOf(false) }
    }

    var searchBarText by remember { mutableStateOf(state.searchQuery) }
    LaunchedEffect(searchBarText) {
        delay(400)
        if (searchBarText != state.searchQuery) {
            viewModel.onEvent(WordEvent.OnSearchQueryChange(searchBarText))
        }
        if (!state.isMultiSelecting) {
            viewModel.onEvent(WordEvent.OnSearch)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(WordEvent.LoadAllWords)
        searchBarText = ""
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
        WordScreenContent(
            words = state.words,
            selectedWords = state.words.filter { state.selectedWords.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
            successMessage = visibleSuccess,
            onEditWord = { word ->
                selectedWord = word
                isDialogOpen = true
            },
            onAddWord = {
                selectedWord = null
                isDialogOpen = true
            },
            onLoadMore = { viewModel.onEvent(WordEvent.LoadMoreAllWords) },
            onWordLongPress = { word ->
                viewModel.onEvent(WordEvent.OnWordLongPress(word.id))
            },
            onWordSelectToggle = { word ->
                viewModel.onEvent(WordEvent.OnWordSelectToggle(word.id))
            },
            searchBarText = searchBarText,
            onSearchBarTextChange = {searchBarText = it}
        )
    }

    if (isDialogOpen) {
        WordFormDialog(
            word = selectedWord,
            errorMessage = visibleError,
            onDismiss = {
                isDialogOpen = false
                selectedWord = null
            },
            onSave = { word, imageUri, audioUri ->
                if (word.id == 0) {
                    viewModel.onEvent(WordEvent.CreateWord(1, word, imageUri, audioUri))
                } else {
                    viewModel.onEvent(WordEvent.UpdateWord(word, imageUri, audioUri))
                }
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
fun WordScreenContent(
    words: List<Word>,
    selectedWords: List<Word>,
    isMultiSelectMode: Boolean,
    isLoading: Boolean,
    isEndReached: Boolean,
    successMessage: String,
    onEditWord: (Word) -> Unit,
    onAddWord: () -> Unit,
    onLoadMore: () -> Unit,
    onWordLongPress: (Word) -> Unit,
    onWordSelectToggle: (Word) -> Unit,
    searchBarText: String,
    onSearchBarTextChange: (String) -> Unit
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
                        .padding(horizontal = MaterialTheme.dimens.small3)
                        .padding(top = MaterialTheme.dimens.small2)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchBarText,
                        onValueChange = onSearchBarTextChange,
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Tìm từ vựng", style = MaterialTheme.typography.titleMedium) },
                        shape = RoundedCornerShape(MaterialTheme.dimens.medium1),
                        singleLine = true,
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(MaterialTheme.dimens.medium2)
                                    .background(
                                        color = Color(0xFF00D9FF),
                                        shape = RoundedCornerShape(MaterialTheme.dimens.medium2)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(MaterialTheme.dimens.small3)
                                )
                            }
                        },
                        colors = defaultTextFieldColors(),
                    )

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

                    Image(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier
                            .size(MaterialTheme.dimens.large)
                            .clickable(onClick = onAddWord)
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().imePadding()
                    .padding(MaterialTheme.dimens.small3),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
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
            OverlaySnackbar(message = successMessage, type = SnackbarType.Success)
        }
    }
}