package com.example.capyvocab_fe.user.test.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.user.test.domain.model.Comment
import java.text.SimpleDateFormat
import java.util.Locale

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
    var showEditDialog by remember { mutableStateOf(false) }
    var editedContent by remember { mutableStateOf(comment.content) }
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
                        text = comment.createdBy.email,
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
                                    showOptions = false
                                    editedContent = comment.content
                                    showEditDialog = true
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

            // Comment timestamp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dateFormat.format(comment.createdAt),
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = "Trả lời",
                    fontSize = 12.sp,
                    color = Color(0xFF42B3FF),
                    modifier = Modifier.clickable { onReply() }
                )
            }
        }
    }
    // Dialog chỉnh sửa comment
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Chỉnh sửa nhận xét") },
            text = {
                OutlinedTextField(
                    value = editedContent,
                    onValueChange = { editedContent = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = defaultTextFieldColors()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEdit(editedContent)
                        showEditDialog = false
                    }
                ) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}
