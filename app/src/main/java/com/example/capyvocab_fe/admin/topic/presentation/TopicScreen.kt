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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.presentation.CourseEvent
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicCard
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.ConfirmDeleteDialog
import com.example.capyvocab_fe.core.ui.components.RippleOverlay
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.core.util.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import kotlinx.coroutines.delay

@Composable
fun TopicScreen(
    course: Course,
    onBackClick: () -> Unit,
    onTopicClick: (Topic) -> Unit,
    viewModel: TopicListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var topicToDelete by remember { mutableStateOf<Topic?>(null) }
    var isDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var isMultiDeleteConfirmDialogOpen by remember { mutableStateOf(false) }

    var visibleError by remember { mutableStateOf("") }

    val multiSelectTransition = if (state.isMultiSelecting) {
        remember { mutableStateOf(true) }
    } else {
        remember { mutableStateOf(false) }
    }

    LaunchedEffect(course) {
        viewModel.onEvent(TopicEvent.LoadTopics(course))
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
    FocusComponent {
        TopicsScreenContent(
            courseTitle = course.title,
            topics = state.topics,
            selectedTopics = state.topics.filter { state.selectedTopics.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
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
            onBackClick = onBackClick,
            onCancelMultiSelect = { viewModel.onEvent(TopicEvent.CancelMultiSelect) },
            onDeleteSelectedTopics = {
                isMultiDeleteConfirmDialogOpen = true
            },
            onTopicLongPress = { topic ->
                viewModel.onEvent(TopicEvent.OnTopicLongPress(topic.id))
            },
            onTopicSelectToggle = { topic ->
                viewModel.onEvent(TopicEvent.OnTopicSelectToggle(topic.id))
            }
        )
    }

    if (isDialogOpen) {
        TopicFormDialog(
            topic = selectedTopic,
            errorMessage = "",
            onDismiss = {
                selectedTopic = null
                isDialogOpen = false
            },
            onSave = {
                topic ->
                if (topic.id == 0) {
                    viewModel.onEvent(TopicEvent.CreateTopic(course, topic))
                } else {
                    viewModel.onEvent(TopicEvent.UpdateTopic(topic))
                }
                isDialogOpen = false
            },
            onDelete = {
                selectedTopic?.let { topic ->
                    viewModel.onEvent(TopicEvent.DeleteTopic(topic.id))
                }
                isDialogOpen = false
            }
        )
    }
    //AlertDialog xác nhận trước khi xoá user
    if (isDeleteConfirmDialogOpen && topicToDelete != null) {
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá người dùng \"${topicToDelete?.title}\" không?",
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

    //multi-user delete confirmation dialog
    if (isMultiDeleteConfirmDialogOpen) {
        val selectedCount = state.selectedTopics.size
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá $selectedCount người dùng đã chọn không?",
            onConfirm = {
                viewModel.onEvent(TopicEvent.OnDeleteSelectedTopics)
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
fun TopicsScreenContent(
    courseTitle: String,
    topics: List<Topic>,
    selectedTopics: List<Topic>,
    isMultiSelectMode: Boolean,
    isLoading: Boolean,
    isEndReached: Boolean,
    onTopicClick: (Topic) -> Unit,
    onAddTopic: () -> Unit,
    onEditTopic: (Topic) -> Unit,
    onBackClick: () -> Unit,
    onLoadMore: () -> Unit,
    onCancelMultiSelect: () -> Unit,
    onDeleteSelectedTopics: () -> Unit,
    onTopicLongPress: (Topic) -> Unit,
    onTopicSelectToggle: (Topic) -> Unit
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
                                text = "Đã chọn ${selectedTopics.size}",
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
                            IconButton(onClick = { onDeleteSelectedTopics() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }

                } else {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onBackClick() },
                            modifier = Modifier.padding(top = 8.dp)) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        // Top bar
                        TopBarTitle(courseTitle, 25.sp)
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
                        placeholder = { Text("Tìm chủ đề") },
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
                            .clickable(onClick = onAddTopic)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
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
                            cardElevation = cardElevation.value
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicScreenPreview() {
    CapyVocab_FETheme {
        val sampleTopic = Topic(
            id = 1,
            title = "Friendship",
            description = "Tình bạn",
            thumbnail = null,
            type = "Free",
            deletedAt = null,
            createdAt = "",
            updatedAt = "",
            displayOrder = 1
        )
        val sampleTopics = listOf(
            sampleTopic,
            sampleTopic.copy(id = 2, title = "Chủ đề số 2 ahihi", type = "Premium"),
            sampleTopic.copy(id = 3, title = "Chủ đề số 3")
        )

        TopicsScreenContent(
            courseTitle = "Tiếng Anh giao tiếp",
            topics = sampleTopics,
            onTopicClick = {},
            onAddTopic = {},
            onEditTopic = {},
            onBackClick = {},
            onLoadMore = {},
            onCancelMultiSelect = {},
            onDeleteSelectedTopics = {},
            onTopicLongPress = {},
            onTopicSelectToggle = {},
            isMultiSelectMode = false,
            isLoading = false,
            isEndReached = false,
            selectedTopics = emptyList()
        )
    }
}
