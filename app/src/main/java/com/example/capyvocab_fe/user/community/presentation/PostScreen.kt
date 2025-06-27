package com.example.capyvocab_fe.user.community.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.ui.theme.MyLightBlue
import com.example.capyvocab_fe.ui.theme.White
import com.example.capyvocab_fe.ui.theme.dimens
import com.example.capyvocab_fe.user.community.domain.model.Comment
import com.example.capyvocab_fe.user.community.domain.model.Post
import com.example.capyvocab_fe.user.community.domain.model.TargetType
import com.example.capyvocab_fe.user.community.presentation.components.ParentCommentCard
import com.example.capyvocab_fe.user.community.presentation.components.PostThumbsGrid
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PostScreen(
    post: Post,
    viewModel: CommunityViewModel = hiltViewModel(),
    onBackClick:() -> Unit,
    navController: NavController
){
    BackHandler(enabled = true) {
        Log.d("PostScreen", "clearScreenPost called")
        viewModel.onEvent(CommunityEvent.ClearScreenPost)
        navController.navigate(Route.UserCoursesScreen.route) {
            popUpTo(Route.UserCoursesScreen.route) {
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    val state by viewModel.state.collectAsState()
    var visibleError by remember { mutableStateOf("") }

    var selectedImage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(Unit) {
        viewModel.onEvent(CommunityEvent.LoadComments(post.id, null))
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

    PostScreenContent(
        post = state.selectedPost!!,
        parentComment = state.selectedPostComment,
        childComment = state.childComment,
        selectedComment = state.selectedComment,
        onImageClick = {imgURL -> selectedImage = imgURL},
        onVoteClick = { post -> viewModel.onEvent(CommunityEvent.VotePost(post))},
        onCreateComment = { content -> viewModel.onEvent(CommunityEvent.CreateComment(content))},
        onBackClick = {
            viewModel.onEvent(CommunityEvent.ClearScreenPost)
            onBackClick()
        },
        onCreateChildCmt = { comment ->
            viewModel.onEvent(CommunityEvent.OnCreateChildCommentMode(comment)) },
        onLoadChildCmt = {comment ->
            viewModel.onEvent(CommunityEvent.LoadComments(post.id, comment.id))
        },
        onCreateParentComment = { viewModel.onEvent(CommunityEvent.OnCreateParentCommentMode) }
    )
}


@Composable
fun PostScreenContent(
    post: Post,
    parentComment: List<Comment>,
    selectedComment: Comment?,
    childComment: Map<Int, List<Comment>>,
    onImageClick:(String) -> Unit,
    onVoteClick:(Post) -> Unit,
    onCreateComment:(String) -> Unit,
    onBackClick:() -> Unit,
    onCreateChildCmt:(Comment) -> Unit,
    onLoadChildCmt:(Comment) -> Unit,
    onCreateParentComment:() -> Unit
)
{
    var commentText by rememberSaveable { mutableStateOf("") }
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(8.dp),
                )
            {
                if(selectedComment != null)
                {
                    Row()
                    {
                        Text("Trả lời ${selectedComment.createdBy?.email}", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            modifier = Modifier.clickable{ onCreateParentComment() },
                            text = "Hủy",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    TextField(
                        value = commentText,
                        onValueChange = { newText -> commentText = newText },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF2F2F2),
                            focusedContainerColor = Color(0xFFF2F2F2),
                            cursorColor = Color(0xFF5E4A45),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        placeholder = {
                            Text("Nhập bình luận...", fontSize = 14.sp, color = Color.Gray)
                        },
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (commentText.isNotBlank()) {
                                onCreateComment(commentText)
                                commentText = ""
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Gửi",
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
                .padding(8.dp,20.dp,8.dp,8.dp)
        )
        {
            Column()
            {
                Row(
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
                        model = post.createdBy.avatar,
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
                            text = post.createdBy.email,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

                        Text(
                            text = sdf.format(post.createdAt),
                            fontWeight = FontWeight.Thin,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(bottom = 72.dp)
                ) {
                    item {
                        Text(
                            text = post.tags?.joinToString("  ") { "#$it" } ?: "",
                            fontSize = 14.sp,
                            color = MyLightBlue,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = post.content ?: "",
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        PostThumbsGrid(
                            images = post.thumbnails,
                            onImageClick = onImageClick
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        HorizontalDivider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${post.commentCount} trả lời, ${post.voteCount} lượt thích",
                                    color = MyLightBlue,
                                    fontSize = 12.sp
                                )

                                IconToggleButton(
                                    checked = post.isAlreadyVote,
                                    onCheckedChange = {
                                        onVoteClick(post)
                                    },
                                    Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (post.isAlreadyVote) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = if (post.isAlreadyVote) "Liked" else "Like",
                                        tint = if (post.isAlreadyVote) Color.Red else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                    itemsIndexed(parentComment) { index, parentComment ->
                        val childComments = childComment[parentComment.id] ?: emptyList()

                        ParentCommentCard(
                            comment = parentComment,
                            childComments = childComments,
                            onCreateChildComment = {onCreateChildCmt(parentComment)},
                            onLoadChildComment = {onLoadChildCmt(parentComment)}
                        )

                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun PostScreenPreview()
{
    val parentCmt = listOf(
        Comment(
            id = 0,
            content = "Licanguli waka waka wwakaa",
            parentComment = null,
            createdBy = User(
                id = 1,
                username = "Aki",
                roleId = 1,
                email = "user@gmail.com",
                avatar = null,
            ),
            createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
            updatedAt = null,
            targetType = TargetType.POST,
            targetId = 1
        ),
        Comment(
            id = 1,
            content = "Nên làm lại",
            parentComment = null,
            createdBy = User(
                id = 1,
                username = "Aki",
                roleId = 1,
                email = "user@gmail.com",
                avatar = null,
            ),
            createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
            updatedAt = null,
            targetType = TargetType.POST,
            targetId = 1
        )
    )


    val childcmt = mapOf(
        0 to listOf(
            Comment(
                id = 0,
                content = "Licanguli",
                parentComment = null,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                targetType = TargetType.POST,
                targetId = 1
            ),
            Comment(
                id = 1,
                content = "Nên làm lại",
                parentComment = null,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                targetType = TargetType.POST,
                targetId = 1
            )
        ),
        1 to listOf(
            Comment(
                id = 0,
                content = "Licanguli",
                parentComment = null,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                targetType = TargetType.POST,
                targetId = 1
            ),
            Comment(
                id = 1,
                content = "Nên làm lại",
                parentComment = null,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                targetType = TargetType.POST,
                targetId = 1
            )
        )
    )

    CapyVocab_FETheme {
        PostScreenContent(
            post = Post(
                id = 1,
                content = "Hi mọi người mình có cái bla bla cần blu blu. mong mng ble ble",
                thumbnails = listOf(),
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
            selectedComment = Comment(
                id = 1,
                content = "Nên làm lại",
                parentComment = null,
                createdBy = User(
                    id = 1,
                    username = "Aki",
                    roleId = 1,
                    email = "user@gmail.com",
                    avatar = null,
                ),
                createdAt = SimpleDateFormat("dd/MM/yyyy").parse("26/05/2025"),
                updatedAt = null,
                targetType = TargetType.POST,
                targetId = 1),
            parentComment = parentCmt,
            childComment = childcmt,
            onImageClick = {},
            onVoteClick =  {},
            onCreateComment = {},
            onBackClick = {},
            onCreateChildCmt = {},
            onLoadChildCmt = {},
            onCreateParentComment = {}
        )
    }
}