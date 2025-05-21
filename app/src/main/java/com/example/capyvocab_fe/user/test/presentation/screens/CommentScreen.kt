package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.domain.model.Comment
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Screen for displaying and adding comments to a folder
 * @param navController Navigation controller for navigating between screens
 * @param folderId ID of the folder being commented on
 * @param state Current UI state
 * @param onEvent Event handler for user actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    navController: NavController,
    folderId: Int,
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top app bar
        TopAppBar(
            title = {
                Text(
                    text = "Nhận xét",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                Text(
                    text = "Sắp theo: Mới đến cũ",
                    color = Color(0xFF42B3FF),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Comments list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(state.comments) { comment ->
                CommentItem(
                    comment = comment,
                    onReply = { /* Handle reply */ },
                    onReport = { /* Handle report */ },
                    onEdit = { content ->
                        onEvent(ExerciseEvent.UpdateComment(folderId, comment.id, content))
                    },
                    onDelete = {
                        onEvent(ExerciseEvent.DeleteComment(folderId, comment.id))
                    },
                    isOwner = comment.createdBy.id == state.currentUser?.id
                )
            }
        }

        // Comment input field
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = { Text("Aa") },
                    shape = RoundedCornerShape(24.dp),
                    colors = defaultTextFieldColors(),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            onEvent(ExerciseEvent.CreateComment(folderId, commentText))
                            commentText = ""
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "Send",
                        tint = Color(0xFF42B3FF)
                    )
                }
            }
        }
    }
}

/**
 * Composable for displaying a single comment
 * @param comment The comment to display
 * @param onReply Callback when the user replies to this comment
 * @param onReport Callback when the user reports this comment
 * @param onEdit Callback when the user edits this comment
 * @param onDelete Callback when the user deletes this comment
 * @param isOwner Whether the current user is the owner of this comment
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentItem(
    comment: Comment,
    onReply: () -> Unit,
    onReport: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: () -> Unit,
    isOwner: Boolean
) {
    var showOptions by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // User avatar
                AsyncImage(
                    model = comment.createdBy.avatar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.default_avt),
                    error = painterResource(R.drawable.default_avt),
                    fallback = painterResource(R.drawable.default_avt)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    // User email
                    Text(
                        text = comment.createdBy.email ?: "dhhh@gmail.com",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    // Comment content
                    Text(
                        text = comment.content,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Options menu
                Box {
                    IconButton(onClick = { showOptions = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.Gray
                        )
                    }

                    DropdownMenu(
                        expanded = showOptions,
                        onDismissRequest = { showOptions = false }
                    ) {
                        if (isOwner) {
                            DropdownMenuItem(
                                text = { Text("Chỉnh sửa") },
                                onClick = {
                                    // Handle edit
                                    showOptions = false
                                    // Show edit dialog
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Xóa") },
                                onClick = {
                                    onDelete()
                                    showOptions = false
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Báo cáo") },
                                onClick = {
                                    onReport()
                                    showOptions = false
                                }
                            )
                        }
                    }
                }
            }

            // Comment timestamp - using current date as fallback since Comment doesn't have createdAt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormat.format(Date()), // Using current date as fallback
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Báo cáo",
                    fontSize = 12.sp,
                    color = Color(0xFF42B3FF),
                    modifier = Modifier.clickable { onReport() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun CommentScreenPreview() {
    CapyVocab_FETheme {
        CommentScreen(
            navController = NavController(LocalContext.current),
            folderId = 1,
            state = ExerciseState(),
            onEvent = {}
        )
    }
}