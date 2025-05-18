package com.example.capyvocab_fe.user.learn.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.admin.word.domain.model.Word
import kotlinx.coroutines.launch

@Composable
fun FlashcardView(
    word: Word?,
    isFront: Boolean,
    onFlip: () -> Unit,
    onContinue: () -> Unit,
    onAlreadyKnow: () -> Unit
) {
    if (word == null) return

    val swipeThreshold = 60f

    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            when {
                                offsetX.value > swipeThreshold -> {
                                    scope.launch {
                                        offsetY.animateTo(300f, tween(300))
                                        onContinue()
                                        offsetX.snapTo(0f)
                                        offsetY.snapTo(0f)
                                    }
                                }

                                offsetX.value < -swipeThreshold -> {
                                    scope.launch {
                                        offsetY.animateTo(300f, tween(300))
                                        onAlreadyKnow()
                                        offsetX.snapTo(0f)
                                        offsetY.snapTo(0f)
                                    }
                                }

                                else -> {
                                    scope.launch {
                                        // nếu chưa đủ ngưỡng thì quay lại vị trí ban đầu
                                        offsetX.animateTo(0f, tween(200))
                                        offsetY.animateTo(0f, tween(200))
                                    }
                                }
                            }
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount)
                                offsetY.snapTo(kotlin.math.abs(offsetX.value) * 0.2f) // bay nhẹ xuống
                            }
                        }
                    )
                }
                .graphicsLayer {
                    translationX = offsetX.value
                    translationY = offsetY.value
                    rotationZ = offsetX.value * 0.01f
                    alpha = 1f - (kotlin.math.abs(offsetX.value) / 600f).coerceIn(0f, 0.4f)
                }
        ) {
            FlipCard(
                isFlipped = !isFront,
                front = {
                    CardFaceFront(word = word, onClick = onFlip)
                },
                back = {
                    CardFaceBack(word = word, onClick = onFlip)
                }
            )
        }

        Spacer(modifier = Modifier.height(33.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Button(
                onClick = onAlreadyKnow,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE5E5E3),
                    contentColor = Color(0xFF5A5A5A)
                )
            ) {
                Text("Đã biết")
            }

            Button(
                onClick = onContinue,
                modifier = Modifier.weight(1f)
            ) {
                Text("Tiếp tục")
            }
        }
    }
}

