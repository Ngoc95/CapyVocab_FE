package com.example.capyvocab_fe.admin.course.presentation

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
import com.example.capyvocab_fe.admin.course.domain.model.CourseLevel
import com.example.capyvocab_fe.admin.course.presentation.components.CourseCard
import com.example.capyvocab_fe.admin.course.presentation.components.CourseFormDialog
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
fun CourseScreen(
    onCourseClick: (Course) -> Unit,
    viewModel: CourseListViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()

    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    var courseToDelete by remember { mutableStateOf<Course?>(null) }
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
            viewModel.onEvent(CourseEvent.OnSearchQueryChange(searchBarText))
        }
        if (!state.isMultiSelecting) {
            viewModel.onEvent(CourseEvent.OnSearch)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(CourseEvent.LoadCourses)
        searchBarText = ""
    }

    //launchEffect to track transition to multi-select mode
    LaunchedEffect(state.isMultiSelecting) {
        multiSelectTransition.value = state.isMultiSelecting
    }
    BackHandler {
        if (state.isMultiSelecting) {
            viewModel.onEvent(CourseEvent.CancelMultiSelect)
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
            selectedCourse = null
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
        CoursesScreenContent(
            courses = state.courses,
            selectedCourses = state.courses.filter { state.selectedCourses.contains(it.id) },
            isMultiSelectMode = state.isMultiSelecting,
            isLoading = state.isLoading,
            isEndReached = state.isEndReached,
            successMessage = visibleSuccess,
            onLoadMore = { viewModel.onEvent(CourseEvent.LoadMoreCourses) },
            onCourseClick = { course ->
                onCourseClick(course)
            },
            onAddCourse = {
                selectedCourse = null
                isDialogOpen = true
            },
            onEditCourse = { course ->
                selectedCourse = course
                isDialogOpen = true
            },
            onCourseLongPress = { course ->
                viewModel.onEvent(CourseEvent.OnCourseLongPress(course.id))
            },
            onCourseSelectToggle = { course ->
                viewModel.onEvent(CourseEvent.OnCourseSelectToggle(course.id))
            },
            searchBarText = searchBarText,
            onSearchBarTextChange = { searchBarText = it }
        )
    }

    if (isDialogOpen) {
        CourseFormDialog(
            course = selectedCourse,
            errorMessage = visibleError,
            onDismiss = {
                selectedCourse = null
                isDialogOpen = false
            },
            onSave = { course ->
                viewModel.onEvent(CourseEvent.SaveCourse(course))
                isDialogOpen = false
            },
            onDelete = {
                selectedCourse?.let {
                    courseToDelete = it
                    isDeleteConfirmDialogOpen = true
                }
                isDialogOpen = false
                selectedCourse = null
            }
        )
    }
    //AlertDialog xác nhận trước khi xoá user
    if (isDeleteConfirmDialogOpen && courseToDelete != null) {
        ConfirmDeleteDialog(
            message = "Bạn có chắc chắn muốn xoá khoá học \"${courseToDelete?.title}\" không?",
            onConfirm = {
                viewModel.onEvent(CourseEvent.DeleteCourse(courseToDelete!!.id))
                isDeleteConfirmDialogOpen = false
                courseToDelete = null
            },
            onDismiss = {
                isDeleteConfirmDialogOpen = false
                courseToDelete = null
            }
        )
    }
}

@Composable
fun CoursesScreenContent(
    courses: List<Course>,
    selectedCourses: List<Course>,
    isMultiSelectMode: Boolean,
    isLoading: Boolean,
    isEndReached: Boolean,
    successMessage: String,
    onCourseClick: (Course) -> Unit,
    onAddCourse: () -> Unit,
    onEditCourse: (Course) -> Unit,
    onLoadMore: () -> Unit,
    onCourseLongPress: (Course) -> Unit,
    onCourseSelectToggle: (Course) -> Unit,
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
                        .padding(horizontal = MaterialTheme.dimens.small3) //12dp
                        .padding(top = MaterialTheme.dimens.small2)
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

                    Spacer(modifier = Modifier.width(MaterialTheme.dimens.small1))

                    Image(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                        modifier = Modifier
                            .size(MaterialTheme.dimens.large)
                            .clickable(onClick = onAddCourse)
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
                itemsIndexed(courses) { index, course ->
                    val isSelected = selectedCourses.contains(course)

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
                        CourseCard(
                            course = course,
                            isMultiSelecting = isMultiSelectMode,
                            isSelected = selectedCourses.contains(course),
                            onClick = { onCourseClick(course) },
                            onEditClick = { onEditCourse(course) },
                            onLongClick = { onCourseLongPress(course) },
                            onCheckedChange = { onCourseSelectToggle(course) },
                            cardElevation = cardElevation.value,
                            isAdmin = true
                        )
                    }
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
            OverlaySnackbar(message = successMessage, type = SnackbarType.Success)
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
            onAddCourse = {},
            onEditCourse = {},
            onLoadMore = {},
            onCourseLongPress = {},
            onCourseSelectToggle = {},
            isMultiSelectMode = false,
            isLoading = false,
            isEndReached = false,
            selectedCourses = emptyList(),
            searchBarText = "",
            onSearchBarTextChange = {},
            successMessage = ""
        )
    }
}



