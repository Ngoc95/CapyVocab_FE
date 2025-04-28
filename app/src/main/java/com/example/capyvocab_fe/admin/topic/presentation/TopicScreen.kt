package com.example.capyvocab_fe.admin.topic.presentation

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
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.topic.domain.model.Topic
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicCard
import com.example.capyvocab_fe.admin.topic.presentation.components.TopicFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.core.util.components.FocusComponent
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun TopicScreen(
    course: Course,
    onTopicClick: (Topic) -> Unit,
    viewModel: TopicListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(course.id) {
        viewModel.onEvent(TopicEvent.LoadTopics(course.id))
    }

    FocusComponent {
        TopicsScreenContent(
            courseTitle = course.title,
            topics = state.topics,
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
//                topic ->
//                if (topic.id == 0) {
//                    viewModel.onEvent(TopicEvent.CreateTopic(courseId, topic))
//                } else {
//                    viewModel.onEvent(TopicEvent.UpdateTopic(topic))
//                }
//                isDialogOpen = false
            },
            onDelete = {
//                selectedTopic?.let { topic ->
//                    viewModel.onEvent(TopicEvent.DeleteTopic(topic.id))
//                }
//                isDialogOpen = false
            }
        )
    }
}

@Composable
fun TopicsScreenContent(
    courseTitle: String,
    topics: List<Topic>,
    onTopicClick: (Topic) -> Unit,
    onAddTopic: () -> Unit,
    onEditTopic: (Topic) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopBarTitle(courseTitle, 25.sp)

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

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            items(
                items = topics,
                key = { it.id }
            ) { topic ->
                TopicCard(
                    topic = topic,
                    onClick = { onTopicClick(topic) },
                    onEditClick = { onEditTopic(topic) }
                )
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
            type = 1,
            totalWords = 50
        )
        val sampleTopics = listOf(
            sampleTopic,
            sampleTopic.copy(id = 2, title = "Chủ đề số 2 ahihi", totalWords = 30, type = 2),
            sampleTopic.copy(id = 3, title = "Chủ đề số 3", totalWords = 70)
        )

        TopicsScreenContent(
            courseTitle = "Tiếng Anh giao tiếp",
            topics = sampleTopics,
            onTopicClick = {},
            onAddTopic = {},
            onEditTopic = {}
        )
    }
}
