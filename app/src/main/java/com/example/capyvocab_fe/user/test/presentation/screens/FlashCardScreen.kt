package com.example.capyvocab_fe.user.test.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.presentation.screens.components.EmptyFlashcardCard
import com.example.capyvocab_fe.user.test.presentation.screens.components.FlashcardItem
import com.example.capyvocab_fe.user.test.presentation.screens.components.NewFlashcardCard
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseState

/**
 * Screen for displaying and editing flashcards
 * @param navController Navigation controller for navigating between screens
 * @param folderId ID of the folder containing flashcards
 * @param state Current UI state
 * @param onEvent Event handler for user actions
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardScreen(
    navController: NavController,
    folderId: Int,
    state: ExerciseState,
    onEvent: (ExerciseEvent) -> Unit,
    isEditing: Boolean = false
) {
    val folder = state.currentFolder
    var searchQuery by remember { mutableStateOf("") }

    // Load folder data when screen is first displayed
    LaunchedEffect(folderId) {
        onEvent(ExerciseEvent.GetFolderById(folderId))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top app bar
        TopAppBar(
            title = {
                Text(
                    text = if (isEditing) "Chỉnh sửa" else "Thẻ ghi nhớ",
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
                if (!isEditing) {
                    Text(
                        text = "Sắp theo: Mặc định",
                        color = Color(0xFF42B3FF),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Search bar (only in edit mode)
        if (isEditing) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Tìm kiếm") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Gray
                    )
                },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                colors = defaultTextFieldColors()
            )
        }

        // Folder title input (only when creating/editing)
        if (isEditing) {
            var folderTitle by remember { mutableStateOf(folder?.name ?: "") }

            OutlinedTextField(
                value = folderTitle,
                onValueChange = { folderTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text("Bài tập chương 3") },
                shape = RoundedCornerShape(8.dp),
                colors = defaultTextFieldColors()
            )
        }

        // Flashcards list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Only show "Add new" section in edit mode
            if (isEditing) {
                item {
                    Text(
                        text = "Thêm từ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                // Add new flashcard card
                item {
                    NewFlashcardCard(
                        selectedImageUri = null,
                        onAddImage = { /* Handle image selection */ }
                    )
                }
            }

            // Existing flashcards
            val flashcards = folder?.flashCards ?: emptyList()

            if (flashcards.isNotEmpty()) {
                item {
                    Text(
                        text = "Danh sách từ vựng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(flashcards) { flashcard ->
                    FlashcardItem(
                        flashcard = flashcard,
                        isEditing = isEditing,
                        onEdit = { /* Handle edit */ },
                        onDelete = { /* Handle delete */ }
                    )
                }
            } else if (!isEditing) {
                // Show empty state when no flashcards and not in edit mode
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không có từ vựng nào",
                            color = Color.Gray
                        )
                    }
                }
            }

            // Empty card at the end (for adding new cards when not in edit mode)
            if (!isEditing && flashcards.isNotEmpty()) {
                item {
                    EmptyFlashcardCard(
                        onAddImage = { /* Handle image selection */ }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Bottom button
        Button(
            onClick = {
                if (isEditing) {
                    // Save changes
                    navController.popBackStack()
                } else {
                    // Navigate to flashcard learning screen
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF42B3FF)
            )
        ) {
            Text(
                text = if (isEditing) "Lưu thay đổi" else "Học bằng flashcard",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun FlashcardScreenPreview() {
    CapyVocab_FETheme {
        FlashcardScreen(
            navController = rememberNavController(),
            folderId = 1,
            state = ExerciseState(),
            onEvent = {},
            isEditing = true
        )
    }
}