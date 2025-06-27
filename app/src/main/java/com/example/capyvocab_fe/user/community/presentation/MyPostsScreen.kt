package com.example.capyvocab_fe.user.community.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.domain.model.User
import com.example.capyvocab_fe.core.ui.components.FocusComponent
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.presentation.components.MyPostCard
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat

@Composable
fun MyPostScreen(
    user: User,
    onPostComment:(Post) -> Unit,
    onEditPost:(Post) -> Unit,
    onBackClick:() -> Unit,
    viewModel: CommunityViewModel = hiltViewModel(),
    navController: NavController
)
{
    BackHandler(enabled = true) {
        Log.d("CreatePostScreen", "clearScreenPost called")
        viewModel.onEvent(CommunityEvent.ClearScreenPost)
        navController.navigate(Route.UserCoursesScreen.route) {
            popUpTo(Route.UserCoursesScreen.route) {
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(CommunityEvent.LoadPostsByOwner(user))
    }
    val state by viewModel.state.collectAsState()

    var visibleError by remember { mutableStateOf("") }

    var selectedImage by remember { mutableStateOf<String?>(null) }


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
        MyPostsScreenContent (
            user = user,
            userPosts = state.selectUserPosts,
            isLoading = state.isLoading,
            isEndReached = state.isEndReachedPost,
            onLoadMore = { viewModel.onEvent(CommunityEvent.LoadMorePostsByOwner(state.selectUser!!))},
            onVoteClick = { post -> viewModel.onEvent(CommunityEvent.VotePost(post))},
            onImageClick = {imgURL -> selectedImage = imgURL},
            onPostComment = {post -> onPostComment(post)},
            onBackClick = {
                viewModel.onEvent(CommunityEvent.ChangeToUserPost)
                onBackClick()
            },
            onEditPost = {post -> onEditPost(post)}
        )
    }

}

@Composable
fun MyPostsScreenContent(
    user: User,
    userPosts: List<Post>?,
    isLoading: Boolean,
    isEndReached: Boolean,
    onBackClick:() -> Unit,
    onLoadMore: () -> Unit,
    onVoteClick: (Post) -> Unit,
    onImageClick: (String) -> Unit,
    onPostComment: (Post) -> Unit,
    onEditPost: (Post) -> Unit
)
{
    Column()
    {
        Row(
            modifier = Modifier.padding(top = 20.dp, start = 8.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.backicon),
                modifier = Modifier.size(40.dp)
                    .clickable { onBackClick() },
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(8.dp))

            AsyncImage(
                model = user.avatar,
                contentDescription = "Avatar",
                placeholder = painterResource(id = R.drawable.default_avt),
                fallback = painterResource(id = R.drawable.default_avt),
                error = painterResource(id = R.drawable.default_avt),
                modifier = Modifier
                    .size(MaterialTheme.dimens.medium2)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = user.username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = user.email,
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp
                )
            }

        }

        if(!userPosts.isNullOrEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                itemsIndexed(userPosts) { index, post ->


                    MyPostCard(
                        post = post,
                        onVoteClick = { onVoteClick(post) },
                        onPostComment = { onPostComment(post) },
                        onImageClick = onImageClick,
                        onClickUserPostsScreen = { },
                        onEditClick = { onEditPost(post) }
                    )
                    if (index >= userPosts.size - 3 && !isLoading && !isEndReached) {
                        onLoadMore()
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MyPostScreenPreview() {
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

        MyPostsScreenContent(
            user = User(
                id = 1,
                username = "w",
                email = "ee",
                avatar = "ee",
                roleId = 1
            ),
            userPosts = SimpleList,
            isLoading = false,
            isEndReached = false,
            onPostComment = { },
            onBackClick = { },
            onVoteClick = { },
            onImageClick = { },
            onLoadMore = { },
            onEditPost = { }
        )

    }
}