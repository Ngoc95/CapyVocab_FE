@file:JvmName("CardFaceKt")

package com.example.capyvocab_fe.user.learn.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capyvocab_fe.admin.word.domain.model.Word
import com.example.capyvocab_fe.core.ui.components.PronunciationPlayer

//@Composable
//fun CardFaceFront(
//    word: Word,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxSize()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = word.content,
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Câu ví dụ (example sentence)
//            word.example?.let { example ->
//                Text(
//                    text = example,
//                    fontSize = 16.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Hình minh họa
//            word.image?.let { imageUrl ->
//                // Dùng Coil hoặc thư viện load ảnh khác để load từ URL
//                AsyncImage(
//                    model = imageUrl,
//                    contentDescription = "Illustration",
//                    modifier = Modifier
//                        .size(150.dp)
//                        .clip(RoundedCornerShape(8.dp)),
//                    contentScale = ContentScale.Fit
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Text(
//                text = "Chạm để xem nghĩa",
//                fontSize = 14.sp,
//                color = Color.Gray,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
//
//@Composable
//fun CardFaceBack(
//    word: Word,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxSize()
//            .clickable(onClick = onClick),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = word.content,
//                fontSize = 28.sp,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Text(
//                text = "(${word.position ?: ""})",
//                fontSize = 16.sp,
//                fontStyle = FontStyle.Italic,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(4.dp))
//
//            Text(
//                text = word.pronunciation ?: "",
//                fontSize = 16.sp,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = word.meaning ?: "",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.SemiBold,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            word.example?.let { example ->
//                Text(
//                    text = example,
//                    fontSize = 16.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            word.translateExample?.let { example ->
//                Text(
//                    text = example,
//                    fontSize = 16.sp,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier.padding(horizontal = 16.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Text(
//                text = "Chạm để lật lại",
//                fontSize = 14.sp,
//                color = Color.Gray,
//                textAlign = TextAlign.Center
//            )
//        }
//    }
//}
@Composable
fun CardFaceFront(
    word: Word,
    onClick: () -> Unit
) {
    val minCardHeight = 500.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minCardHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Phần nội dung chính + hình ảnh
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PronunciationPlayer(audioUrl = word.audio ?: "", wordId = word.id)

                Text(
                    text = word.content,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                word.example?.let { example ->
                    Text(
                        text = example,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                word.image?.let { imageUrl ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Illustration",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // Phần cố định bên dưới
            Text(
                text = "Chạm để xem nghĩa",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun CardFaceBack(
    word: Word,
    onClick: () -> Unit
) {
    val minCardHeight = 500.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = minCardHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            PronunciationPlayer(audioUrl = word.audio ?: "", wordId = word.id)

            Spacer(modifier = Modifier.weight(0.3f))

            Text(
                text = word.content,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "(${word.position ?: ""})",
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            Text(
                text = word.pronunciation ?: "",
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = word.meaning ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            word.example?.let { example ->
                Text(
                    text = example,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            word.translateExample?.let { translated ->
                Text(
                    text = translated,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Chạm để lật lại",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
