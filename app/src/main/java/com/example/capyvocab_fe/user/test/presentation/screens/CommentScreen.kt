package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.presentation.screens.components.CommentItem
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseState

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
    var sortDescending by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top app bar - this will stay fixed
        TopAppBar(
            title = {
                Text(
                    text = "Nhận xét",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                TextButton(onClick = { sortDescending = !sortDescending }) {
                    Text(
                        text = if (sortDescending) "Mới đến cũ" else "Cũ đến mới",
                        color = Color(0xFF42B3FF)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Comments list - only this part will scroll
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            val sortedComments =
                if (sortDescending) state.comments.sortedByDescending { it.createdAt }
                else state.comments.sortedBy { it.createdAt }
            items(sortedComments) { comment ->
                CommentItem(
                    comment = comment,
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

        // Comment input field - this will stay at bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
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
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (commentText.isNotBlank()) {
                            onEvent(ExerciseEvent.CreateComment(folderId, commentText.trim()))
                            commentText = ""
                        }
                        focusManager.clearFocus()
                    }
                )
            )
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
            onEvent = {},
        )
    }
}