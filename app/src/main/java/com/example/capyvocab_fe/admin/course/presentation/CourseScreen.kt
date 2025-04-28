package com.example.capyvocab_fe.admin.course.presentation

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.admin.course.presentation.components.CourseCard
import com.example.capyvocab_fe.admin.course.presentation.components.CourseFormDialog
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.core.ui.components.TopBarTitle
import com.example.capyvocab_fe.core.util.components.FocusComponent
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme

@Composable
fun CourseScreen(
    onCourseClick: (Course) -> Unit,
    viewModel: CourseListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onEvent(CourseEvent.LoadCourses)
    }

    FocusComponent {
        CoursesScreenContent(
            courses = state.courses,
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
            }
        )
    }

    if (isDialogOpen) {
        CourseFormDialog(
            course = selectedCourse,
            errorMessage = "",
            onDismiss = {
                selectedCourse = null
                isDialogOpen = false
            },
            onSave = { course ->
                if (course.id == 0) {
                    viewModel.onEvent(CourseEvent.CreateCourse(course))
                } else {
                    viewModel.onEvent(CourseEvent.UpdateCourse(course))
                }
                isDialogOpen = false
            },
            onDelete = {
                selectedCourse?.let { course ->
                    viewModel.onEvent(CourseEvent.DeleteCourse(course.id))
                }
                isDialogOpen = false
            }
        )
    }
}

@Composable
fun CoursesScreenContent(
    courses: List<Course>,
    onCourseClick: (Course) -> Unit,
    onAddCourse: () -> Unit,
    onEditCourse: (Course) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top bar
        TopBarTitle("Khóa học")

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
                placeholder = { Text("Tìm khóa học") },
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
                    .clickable(onClick = onAddCourse)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            items(courses, key = { it.id }) { course ->
                CourseCard(
                    course = course,
                    onClick = { onCourseClick(course) },
                    onEditClick = { onEditCourse(course) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CoursesScreenContentPreview() {
    CapyVocab_FETheme{
        val sampleCourses = listOf(
            Course(
                id = 1,
                title = "Tiếng Anh A1",
                level = 1,
                target = "Người mới bắt đầu",
                description = "Khóa học tiếng Anh cơ bản trình độ A1",
                totalTopics = 10
            ),
            Course(
                id = 2,
                title = "Ngữ pháp cơ bản",
                level = 1,
                target = "Nắm chắc ngữ pháp nền tảng",
                description = "Ngữ pháp phổ biến trong tiếng Anh giúp người mới hiểu được cơ bản cấu trúc",
                totalTopics = 8
            ),
            Course(
                id = 3,
                title = "Từ vựng nâng cao",
                level = 2,
                target = "Mở rộng vốn từ chuyên sâu",
                description = "Từ vựng học thuật và chuyên ngành",
                totalTopics = 12
            )
        )

        CoursesScreenContent(
            courses = sampleCourses,
            onCourseClick = {},
            onAddCourse = {},
            onEditCourse = {}
        )
    }
}



