package com.example.capyvocab_fe.admin.topic.presentation

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicCard
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.core.ui.components.OverlaySnackbar
import com.example.capyvocab_fe.core.ui.components.SnackbarType
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.dimens
import kotlinx.coroutines.delay

@Composable
fun TopicsInCourseScreen(
    course: Course,
    onTopicClick: (Topic) -> Unit,
    viewModel: TopicListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var topicToDelete by remember { mutableStateOf<Topic?>(null) }
    var isDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var visibleError by remember { mutableStateOf("") }
    var visibleSuccess by remember { mutableStateOf("") }

    val multiSelectTransition = if (state.isMultiSelecting) {
        remember { mutableStateOf(true) }
    } else {
        remember { mutableStateOf(false) }
    }

    LaunchedEffect(course) {
        viewModel.onEvent(TopicEvent.LoadTopics(course))
    }
    var searchBarText by remember { mutableStateOf(state.searchQuery) }
    LaunchedEffect(searchBarText) {
        delay(400)
        if (searchBarText != state.searchQuery) {
            viewModel.onEvent(TopicEvent.OnSearchQueryChange(searchBarText))
        }
        if (!state.isMultiSelecting) {
            viewModel.onEvent(TopicEvent.OnSearch)
        }
    }

    //launchEffect to track transition to multi-select mode
    LaunchedEffect(state.isMultiSelecting) {
        multiSelectTransition.value = state.isMultiSelecting
    }
    BackHandler {
        if (state.isMultiSelecting) {
            viewModel.onEvent(TopicEvent.CancelMultiSelect)
        } else {
            navController.navigate(Route.CoursesScreen.route) {
                popUpTo(Route.CoursesScreen.route) {
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
            selectedTopic = null
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
        TopicsInCourseScreenContent(
            topics = state.topics,
            selectedTopics = state.topics.filter { state.selectedTopics.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
            successMessage = visibleSuccess,
            onLoadMore = { viewModel.onEvent(TopicEvent.LoadMoreTopics(course)) },
            onTopicClick = { topic ->
                onTopicClick(topic)
            },
            onAddTopic = {
                selectedTopic = null
                isDialogOpen = true
            },
            onEditTopic = { topic ->
                selectedTopic = topic
                isDialogOpen = true
            },
            onTopicLongPress = { topic ->
                viewModel.onEvent(TopicEvent.OnTopicLongPress(topic.id))
            },
            onTopicSelectToggle = { topic ->
                viewModel.onEvent(TopicEvent.OnTopicSelectToggle(topic.id))
            },
            searchBarText = searchBarText,
            onSearchBarTextChange = {searchBarText = it}
        )
    }

    if (isDialogOpen) {
        TopicFormDialog(
            topic = selectedTopic,
            errorMessage = visibleError,
            onDismiss = {
                selectedTopic = null
                isDialogOpen = false
            },
            onSave = { topic, imageUri ->
                if (topic.id == 0) {
                    viewModel.onEvent(TopicEvent.CreateTopic(course.id, topic, imageUri))
                } else {
                    viewModel.onEvent(TopicEvent.UpdateTopic(topic, imageUri))
                }
            },
            onDelete = {
                selectedTopic?.let {
                    topicToDelete = it
                    isDeleteConfirmDialogOpen = true
                }
                isDialogOpen = false
                selectedTopic = null
            }
        )
    }
    //AlertDialog xác nhận trước khi xoá user
    if (isDeleteConfirmDialogOpen && topicToDelete != null) {
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá chủ đề \"${topicToDelete?.title}\" không?",
            onConfirm = {
                viewModel.onEvent(TopicEvent.DeleteTopic(topicToDelete!!.id))
                isDeleteConfirmDialogOpen = false
                topicToDelete = null
            },
            onDismiss = {
                isDeleteConfirmDialogOpen = false
                topicToDelete = null
            }
        )
    }
}

@Composable
fun TopicsInCourseScreenContent(
    topics: List<Topic>,
    selectedTopics: List<Topic>,
    isMultiSelectMode: Boolean,
    isLoading: Boolean,
    isEndReached: Boolean,
    successMessage: String,
    onTopicClick: (Topic) -> Unit,
    onAddTopic: () -> Unit,
    onEditTopic: (Topic) -> Unit,
    onLoadMore: () -> Unit,
    onTopicLongPress: (Topic) -> Unit,
    onTopicSelectToggle: (Topic) -> Unit,
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
                        placeholder = { Text("Tìm chủ đề", style = MaterialTheme.typography.titleMedium) },
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
                            .clickable(onClick = onAddTopic)
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize().imePadding()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                itemsIndexed(topics) { index, topic ->
                    val isSelected = selectedTopics.contains(topic)

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
                        TopicCard(
                            topic = topic,
                            onClick = { onTopicClick(topic) },
                            onEditClick = { onEditTopic(topic) },
                            onLongClick = { onTopicLongPress(topic) },
                            onCheckedChange = { onTopicSelectToggle(topic) },
                            isMultiSelecting = isMultiSelectMode,
                            isSelected = isSelected,
                            cardElevation = cardElevation.value,
                            isAdmin = true
                        )
                    }
                    // Load thêm nếu gần cuối
                    if (index >= topics.size - 3 && !isLoading && !isEndReached) {
                        onLoadMore()
                    }
                }
                // loading indicator khi đang load thêm
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
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

@Preview(showBackground = true)
@Composable
fun TopicsInCourseScreenPreview() {
    CapyVocab_FETheme {
        val sampleTopic = Topic(
            id = 1,
            title = "Friendship",
            description = "Tình bạn",
            thumbnail = "",
            type = "Free",
            alreadyLearned = false
        )
        val sampleTopics = listOf(
            sampleTopic,
            sampleTopic.copy(id = 2, title = "Chủ đề số 2 ahihi", type = "Premium"),
            sampleTopic.copy(id = 3, title = "Chủ đề số 3")
        )

        TopicsInCourseScreenContent(
            topics = sampleTopics,
            onTopicClick = {},
            onAddTopic = {},
            onEditTopic = {},
            onLoadMore = {},
            onTopicLongPress = {},
            onTopicSelectToggle = {},
            isMultiSelectMode = false,
            isLoading = false,
            isEndReached = false,
            successMessage = "",
            selectedTopics = emptyList(),
            searchBarText = "",
            onSearchBarTextChange = {}
        )
    }
}
