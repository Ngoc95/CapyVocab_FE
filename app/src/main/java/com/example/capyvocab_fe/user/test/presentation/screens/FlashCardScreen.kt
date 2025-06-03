package com.example.capyvocab_fe.user.test.presentation.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.capyvocab_fe.auth.presentation.ui.components.defaultTextFieldColors
import com.example.capyvocab_fe.navigation.Route
import com.example.capyvocab_fe.ui.theme.CapyVocab_FETheme
import com.example.capyvocab_fe.user.test.data.remote.model.FlashCardRequest
import com.example.capyvocab_fe.user.test.data.remote.model.UpdateFolderRequest
import com.example.capyvocab_fe.user.test.domain.model.FlashCard
import com.example.capyvocab_fe.user.test.presentation.screens.components.FlashcardItem
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseState
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseViewModel
import kotlinx.coroutines.launch

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
    isEditing: Boolean = false,
    viewModel: ExerciseViewModel = hiltViewModel()
) {
    var editingMode by remember { mutableStateOf(isEditing) }
    val isCreator = state.currentFolder?.createdBy?.id == state.currentUser?.id
    val folder = state.currentFolder
    var searchQuery by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val editingFlashcards = remember { mutableStateListOf<FlashCard>() }

    val flashcardsToDisplay = if (editingMode) {
        editingFlashcards
    } else {
        folder?.flashCards.orEmpty().filter {
            val query = searchQuery.trim().lowercase()
            it.frontContent.lowercase().contains(query) || it.backContent.lowercase().contains(query)
        }
    }

    val selectedImageIndex = remember { mutableIntStateOf(-1) }
    val selectedImageSide = remember { mutableStateOf("front") } // "front" hoặc "back"

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                coroutineScope.launch {
                    val result = viewModel.uploadImage(it)
                    result.fold(
                        ifLeft = {
                            Toast.makeText(context, "Upload thất bại", Toast.LENGTH_SHORT).show()
                        },
                        ifRight = { url ->
                            val index = selectedImageIndex.intValue
                            if (index in editingFlashcards.indices) {
                                val current = editingFlashcards[index]
                                val updated = if (selectedImageSide.value == "front") {
                                    current.copy(frontImage = url)
                                } else {
                                    current.copy(backImage = url)
                                }
                                editingFlashcards[index] = updated
                            }
                        }
                    )
                }
            }
        }
    )

    // Dialog cảnh báo khi lưu
    var showSaveErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(folderId) {
        onEvent(ExerciseEvent.GetFolderById(folderId))
    }

    // Copy dữ liệu flashcard khi bật editing mode
    LaunchedEffect(editingMode, folder?.flashCards) {
        if (editingMode && folder?.flashCards != null) {
            editingFlashcards.clear()
            editingFlashcards.addAll(folder.flashCards)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopAppBar(
            title = { Text("Thẻ ghi nhớ", fontSize = 18.sp, fontWeight = FontWeight.Medium) },
            navigationIcon = {
                IconButton(onClick = {
                    if (editingMode) {
                        // Hủy chỉnh sửa: reset flashcards và thoát editing mode
                        editingFlashcards.clear()
                        folder?.flashCards?.let { editingFlashcards.addAll(it) }
                        editingMode = false
                    } else {
                        navController.popBackStack()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                if (isCreator) {
                    Text(
                        text = if (editingMode) "Lưu" else "Chỉnh sửa",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                if (editingMode) {
                                    // Validate trước khi lưu
                                    val hasEmptyCard = editingFlashcards.any {
                                        it.frontContent.isBlank() || it.backContent.isBlank()
                                    }
                                    if (hasEmptyCard) {
                                        showSaveErrorDialog = true
                                    } else {
                                        // Lưu thay đổi
                                        editingMode = false
                                        val flashcardRequests = editingFlashcards.map {
                                            FlashCardRequest(
                                                frontContent = it.frontContent,
                                                frontImage = it.frontImage,
                                                backContent = it.backContent,
                                                backImage = it.backImage
                                            )
                                        }
                                        onEvent(
                                            ExerciseEvent.UpdateFolder(
                                                id = folderId,
                                                request = UpdateFolderRequest(
                                                    name = folder?.name ?: "",
                                                    flashCards = flashcardRequests,
                                                    quizzes = null
                                                )
                                            )
                                        )
                                    }
                                } else {
                                    // Bật chỉnh sửa
                                    editingMode = true
                                }
                            }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        if (!editingMode) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Tìm kiếm") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                colors = defaultTextFieldColors()
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (flashcardsToDisplay.isNotEmpty()) {
                item {
                    Text(
                        text = "Danh sách từ vựng",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                itemsIndexed(flashcardsToDisplay) { index, flashcard ->
                    FlashcardItem(
                        flashcard = flashcard,
                        isEditing = editingMode,
                        searchQuery = searchQuery,
                        onUpdate = {
                            if (index in editingFlashcards.indices) {
                                editingFlashcards[index] = it
                            }
                        },
                        onDelete = {
                            if (index in editingFlashcards.indices) {
                                editingFlashcards.removeAt(index)
                            }
                        },
                        onSelectFrontImage = {
                            selectedImageIndex.intValue = index
                            selectedImageSide.value = "front"
                            imagePickerLauncher.launch("image/*")
                        },
                        onSelectBackImage = {
                            selectedImageIndex.intValue = index
                            selectedImageSide.value = "back"
                            imagePickerLauncher.launch("image/*")
                        }
                    )
                }
            } else {
                item{
                    // Hiển thị thông báo khi không có câu hỏi
                    Box(
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Không có từ vựng nào",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            if (editingMode) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Nhấn nút + để thêm từ vựng mới",
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        // Nút thêm flashcard nếu đang chỉnh sửa
        if (editingMode && isCreator) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                FloatingActionButton(
                    onClick = {
                        editingFlashcards.add(
                            FlashCard(
                                frontContent = "",
                                frontImage = "N/A",
                                backContent = "",
                                backImage = "N/A"
                            )
                        )
                        // Cuộn xuống phần tử cuối sau khi thêm
                        coroutineScope.launch {
                            listState.animateScrollToItem(editingFlashcards.lastIndex)
                        }
                    },
                    containerColor = Color(0xFF42B3FF),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
                }
            }
        }

        // Nút học
        if (!editingMode && flashcardsToDisplay.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF4FC3F7), Color(0xFF1565C0))
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { navController.navigate("${Route.FlashCardLearningScreen}/$folderId") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Học bằng flashcard",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    // Dialog cảnh báo khi lưu lỗi
    if (showSaveErrorDialog) {
        Dialog(onDismissRequest = { showSaveErrorDialog = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Có lỗi khi lưu!", color = Color.Red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { showSaveErrorDialog = false }) {
                        Text("Đóng")
                    }
                }
            }
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