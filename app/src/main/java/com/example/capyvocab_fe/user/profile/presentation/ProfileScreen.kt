package com.example.capyvocab_fe.user.profile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.learn.presentation.LearnViewModel

@Composable
fun ProfileScreen(
    onCourseClick: (Course) -> Unit,
    viewModel: LearnViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

}

@Composable
fun ProfileScreenContent(
    isLoading: Boolean,
    onLoadMore: () -> Unit,
) {}

@Preview(showBackground = true)
@Composable
fun ProfileScreenContentPreview() {
    CapyVocab_FETheme {
        ProfileScreenContent(
            isLoading = false,
        ){

        }

    }
}

