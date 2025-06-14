package com.example.capyvocab_fe.user.learn.presentation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.domain.model.CourseLevel
import com.example.capyvocab_fe.admin.course.presentation.CourseEvent
import com.example.capyvocab_fe.admin.course.presentation.components.CourseCard
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.navigator.components.UserTopBar
import kotlinx.coroutines.delay

@Composable
fun CourseScreen(
    onCourseClick: (Course) -> Unit,
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    var selectedCourse by remember { mutableStateOf<Course?>(null) }

    var visibleError by remember { mutableStateOf("") }

    var searchBarText by remember { mutableStateOf(state.searchQuery) }
    LaunchedEffect(searchBarText) {
        delay(400)
        if (searchBarText != state.searchQuery) {
            viewModel.onEvent(LearnEvent.OnSearchQueryChange(searchBarText))
            viewModel.onEvent(LearnEvent.OnSearch)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(LearnEvent.LoadCourses)
        searchBarText = ""
    }

    // Khi errorMessage thay đổi, show snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    FocusComponent {
        CoursesScreenContent(
            courses = state.courses,
            isLoading = state.isLoading,
            isEndReached = state.isEndReachedCourse,
            onLoadMore = { viewModel.onEvent(LearnEvent.LoadMoreCourses) },
            onCourseClick = { course ->
                onCourseClick(course)
            },
            selectedCourse = selectedCourse,
            searchBarText = searchBarText,
            onSearchBarTextChange = { searchBarText = it}
        )
    }
}

@Composable
fun CoursesScreenContent(
    courses: List<Course>,
    isLoading: Boolean,
    isEndReached: Boolean,
    onCourseClick: (Course) -> Unit,
    onLoadMore: () -> Unit,
    selectedCourse: Course?,
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
            // Top bar
            UserTopBar()

            // Search bar
            Row(
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.dimens.small3) //12dp
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchBarText,
                    onValueChange = onSearchBarTextChange,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tìm khóa học", style = MaterialTheme.typography.titleMedium) },
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
            }

            Spacer(modifier = Modifier.height(MaterialTheme.dimens.small3))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.medium1)
            ) {
                itemsIndexed(courses) { index, course ->
                    val isSelected = selectedCourse == course

                    CourseCard(
                        course = course,
                        isMultiSelecting = false,
                        isSelected = isSelected,
                        onClick = { onCourseClick(course) },
                        onEditClick = { },
                        onLongClick = { },
                        onCheckedChange = { },
                        isAdmin = false
                    )
                    // Load thêm nếu gần cuối
                    if (index >= courses.size - 3 && !isLoading && !isEndReached) {
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
fun CoursesScreenContentPreview() {
    CapyVocab_FETheme {
        val sampleCourses = listOf(
            Course(
                id = 1,
                title = "Tiếng Anh A1",
                level = CourseLevel.BEGINNER.value,
                target = "Người mới bắt đầu",
                description = "Khóa học tiếng Anh cơ bản trình độ A1",
                courseTopics = emptyList()
            ),
            Course(
                id = 2,
                title = "Ngữ pháp cơ bản",
                level = CourseLevel.INTERMEDIATE.value,
                target = "Nắm chắc ngữ pháp nền tảng",
                description = "Ngữ pháp phổ biến trong tiếng Anh giúp người mới hiểu được cơ bản cấu trúc",
                courseTopics = emptyList()
            ),
            Course(
                id = 3,
                title = "Từ vựng nâng cao",
                level = CourseLevel.ADVANCE.value,
                target = "Mở rộng vốn từ chuyên sâu",
                description = "Từ vựng học thuật và chuyên ngành",
                courseTopics = emptyList()
            )
        )

        CoursesScreenContent(
            courses = sampleCourses,
            onCourseClick = {},
            onLoadMore = {},
            isLoading = false,
            isEndReached = false,
            selectedCourse = sampleCourses[1],
            searchBarText = "",
            onSearchBarTextChange = {}
        )
    }
}