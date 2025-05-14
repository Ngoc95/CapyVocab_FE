package com.example.capyvocab_fe.user.learn.presentation


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicCard
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.core.util.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import kotlinx.coroutines.delay

@Composable
fun TopicsInCourseScreen(
    course: Course,
    onTopicClick: (Topic) -> Unit,
    onBackClick: () -> Unit,
    viewModel: LearnViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var visibleError by remember { mutableStateOf("") }


    LaunchedEffect(course) {
        viewModel.onEvent(LearnEvent.LoadTopics(course))
    }

    BackHandler {
        navController.navigate(Route.UserCoursesScreen.route) {
            popUpTo(Route.UserCoursesScreen.route) {
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
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
            selectedTopic = null
        }
    }


    FocusComponent {
        TopicsInCourseScreenContent(
            courseTitle = course.title,
            topics = state.topics,
            isLoading = state.isLoading,
            isEndReached = state.isEndReachedTopic,
            onLoadMore = { viewModel.onEvent(LearnEvent.LoadMoreTopics(course)) },
            onTopicClick = { topic ->
                onTopicClick(topic)
            },
            selectedTopic = selectedTopic,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun TopicsInCourseScreenContent(
    courseTitle: String,
    topics: List<Topic>,
    isLoading: Boolean,
    isEndReached: Boolean,
    onTopicClick: (Topic) -> Unit,
    onLoadMore: () -> Unit,
    onBackClick: () -> Unit,
    selectedTopic: Topic?
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
            //Top bar
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

            // Search bar
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

            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                itemsIndexed(topics) { index, topic ->
                    val isSelected = selectedTopic == topic

                    Box() {
                        TopicCard(
                            topic = topic,
                            onClick = { onTopicClick(topic) },
                            onEditClick = { },
                            onLongClick = { },
                            onCheckedChange = { },
                            isMultiSelecting = false,
                            isSelected = isSelected,
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
fun TopicsInCourseScreenPreview() {
    CapyVocab_FETheme {
        val sampleTopic = Topic(
            id = 1,
            title = "Friendship",
            description = "Tình bạn",
            thumbnail = "",
            type = "Free"
        )
        val sampleTopics = listOf(
            sampleTopic,
            sampleTopic.copy(id = 2, title = "Chủ đề số 2 ahihi", type = "Premium"),
            sampleTopic.copy(id = 3, title = "Chủ đề số 3")
        )

        TopicsInCourseScreenContent(
            courseTitle = "Hello",
            topics = sampleTopics,
            onTopicClick = {},
            onLoadMore = {},
            isLoading = false,
            isEndReached = false,
            selectedTopic = sampleTopics[1],
            onBackClick = {}
        )
    }
}