package com.example.capyvocab_fe.user.community.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.course.domain.model.Course
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.presentation.components.ExpandableFAB
import com.example.capyvocab_fe.user.community.presentation.components.PostCard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat


@Composable
fun CommunityScreen(
    onPostComment:(Post) -> Unit,
    onCreatePost:() -> Unit,
    onMyPost:() -> Unit,
    onClickUserPostsScreen: (User) -> Unit,
    viewModel: CommunityViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    var selectedPost by remember { mutableStateOf<Course?>(null) }

    var visibleError by remember { mutableStateOf("") }

    var selectedImage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        viewModel.onEvent(CommunityEvent.LoadPosts)
    }

    // Khi errorMessage thay đổi, show snackbar trong 3 giây
    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage.isNotEmpty()) {
            visibleError = state.errorMessage
            delay(3000) // hiện 3 giây
            visibleError = "" // ẩn sau 3 giây
        }
    }

    if (selectedImage != null) {
        Dialog( onDismissRequest = { selectedImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { selectedImage = null },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    FocusComponent {
        CommunityScreenContent(
            posts = state.posts,
            isLoading = state.isLoading,
            isEndReached = state.isEndReachedPost,
            onLoadMore = { viewModel.onEvent(CommunityEvent.LoadMorePosts)},
            onVoteClick = { post -> viewModel.onEvent(CommunityEvent.VotePost(post))},
            onImageClick = {imgURL -> selectedImage = imgURL},
            selectedPost = state.selectedPost,
            onPostComment = {post -> onPostComment(post)},
            onCreatePost = { onCreatePost() },
            onClickUserPostsScreen = {user ->
                onClickUserPostsScreen(user)
            },
            onMyPosts = { onMyPost() }
        )
    }
}

@Composable
fun CommunityScreenContent(
    posts: List<Post>,
    isLoading: Boolean,
    isEndReached: Boolean,
    onLoadMore: () -> Unit,
    onVoteClick: (Post) -> Unit,
    onImageClick: (String) -> Unit,
    onPostComment: (Post) -> Unit,
    onCreatePost:() -> Unit,
    onClickUserPostsScreen:(User) -> Unit,
    onMyPosts:() -> Unit,
    selectedPost: Post?
)
{
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

    Scaffold(
        floatingActionButton = {
            ExpandableFAB(
                onCreatePost = { onCreatePost() },
                onMyPosts = { onMyPosts() }
            )
        }

    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    itemsIndexed(posts) { index, post ->
                        val isSelected = selectedPost == post

                        PostCard(
                            post = post,
                            onVoteClick = { onVoteClick(post) },
                            onPostComment = { onPostComment(post) },
                            onImageClick = onImageClick,
                            onClickUserPostsScreen = {user -> onClickUserPostsScreen(user)}
                        )
                        if (index >= posts.size - 3 && !isLoading && !isEndReached) {
                            onLoadMore()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Colors(
    backgroundColor: Color,
    focusedIndicatorColor: Color,
    unfocusedIndicatorColor: Color,
    textColor: Color
) {
    TODO("Not yet implemented")
}


@Preview(showBackground = true)
@Composable
fun CommunitycreenPreview() {
    CapyVocab_FETheme {
        val SimpleList = listOf(
            Post(
                id = 1,
                content = "Hi mọi người mình có cái bla bla cần blu blu. mong mng ble ble",
                thumbnails = listOf("1", "2"),
                tags = listOf("IELTS", "Listen"),
                commentCount = 11,
                voteCount = 200,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                isAlreadyVote = true,
            ),
            Post(
                id = 1,
                content = "Hi mọi người mình có cái bla bla cần blu blu. mong mng ble ble",
                thumbnails = listOf("1", "2"),
                tags = listOf("IELTS", "Listen"),
                commentCount = 11,
                voteCount = 200,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                isAlreadyVote = true,
            )
        )

        CommunityScreenContent(
            posts = SimpleList,
            isLoading = false,
            isEndReached = false,
            onLoadMore = {},
            onVoteClick = { },
            selectedPost = null,
            onImageClick = {},
            onPostComment = {},
            onCreatePost = {},
            onClickUserPostsScreen = { },
            onMyPosts = { }
        )

    }
}