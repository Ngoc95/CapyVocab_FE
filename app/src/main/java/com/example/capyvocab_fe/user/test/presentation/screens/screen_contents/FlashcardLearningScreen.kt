package com.example.capyvocab_fe.user.test.presentation.screens.screen_contents

import android.media.MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.capyvocab_fe.R
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseEvent
import com.example.capyvocab_fe.user.test.presentation.viewmodel.ExerciseViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardLearningScreen(
    folderId: Int,
    viewModel: ExerciseViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.flip)
    }

    val state = viewModel.state.collectAsState().value
    val flashcards = state.currentFolder?.flashCards.orEmpty()

    var currentIndex by remember { mutableIntStateOf(0) }
    var isFront by remember { mutableStateOf(true) }

    val currentCard = flashcards.getOrNull(currentIndex)

    val rotation by animateFloatAsState(
        targetValue = if (isFront) 0f else 180f,
        animationSpec = tween(durationMillis = 400),
        label = "FlipRotation"
    )

    LaunchedEffect(folderId) {
        viewModel.onEvent(ExerciseEvent.GetFolderById(folderId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Học flashcard",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    Text(
                        text = "Xong",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF42B3FF),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { onBack() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentCard == null) {
                Text("Không có thẻ nào để học", color = Color.Gray)
            } else {
                // Trung tâm màn hình
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    // Viền xám sát với Card
                    Box(
                        modifier = Modifier
                            .width(330.dp)
                            .height(450.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Transparent)
                            .pointerInput(currentIndex) {
                                detectDragGestures(
                                    onDragEnd = {},
                                    onDragCancel = {},
                                    onDrag = { change, dragAmount ->
                                        val (dx, _) = dragAmount
                                        if (abs(dx) > 100) { // threshold khoảng cách kéo
                                            if (dx > 0 && currentIndex > 0) {
                                                currentIndex--
                                                isFront = true
                                            } else if (dx < 0 && currentIndex < flashcards.lastIndex) {
                                                currentIndex++
                                                isFront = true
                                            }
                                            mediaPlayer.seekTo(0)
                                            mediaPlayer.start()
                                        }
                                    }
                                )
                            }
                            .clickable {
                                isFront = !isFront
                                mediaPlayer.seekTo(0)
                                mediaPlayer.start()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        // Card với hiệu ứng flip
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp),
                            modifier = Modifier
                                .width(310.dp)
                                .height(430.dp)
                                .graphicsLayer {
                                    rotationY = rotation
                                    cameraDistance = 8 * density
                                }
                        ) {
                            val isBackVisible = rotation > 90f
                            val displayedRotation = if (isBackVisible) 180f else 0f

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        rotationY = displayedRotation
                                    }
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                val content = if (isBackVisible) currentCard.backContent else currentCard.frontContent
                                val image = if (isBackVisible) currentCard.backImage else currentCard.frontImage

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    image?.takeIf { it != "N/A" }?.let {
                                        AsyncImage(
                                            model = it,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(200.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                    Text(
                                        text = content.orEmpty(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                // Nút điều hướng
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    GradientCircleButton(
                        icon = Icons.Default.KeyboardArrowLeft,
                        onClick = {
                            if (currentIndex > 0) {
                                currentIndex--
                                isFront = true
                            }
                        },
                        enabled = currentIndex > 0
                    )

                    GradientCircleButton(
                        icon = Icons.Default.KeyboardArrowRight,
                        onClick = {
                            if (currentIndex < flashcards.lastIndex) {
                                currentIndex++
                                isFront = true
                            }
                        },
                        enabled = currentIndex < flashcards.lastIndex
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun GradientCircleButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val gradient = if (enabled)
        Brush.horizontalGradient(listOf(Color(0xFF4FC3F7), Color(0xFF1565C0)))
    else
        Brush.horizontalGradient(listOf(Color.LightGray, Color.Gray))

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(gradient)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
    }
}
