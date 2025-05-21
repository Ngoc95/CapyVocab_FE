package com.example.capyvocab_fe.user.learn.presentation.components

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.capyvocab_fe.R

@Composable
fun PronunciationPlayer(
    audioUrl: String,
    wordId: Int
) {
    val context = LocalContext.current
    var hasPlayed by rememberSaveable(wordId) { mutableStateOf(false) }

    fun playAudio(speed: Float) {
        try {
            val mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(audioUrl)
            mediaPlayer.setOnPreparedListener {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(speed)
                }
                mediaPlayer.start()
            }
            mediaPlayer.setOnCompletionListener {
                mediaPlayer.release()
            }
            mediaPlayer.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Tự động phát tốc độ thường khi hiển thị
    LaunchedEffect(wordId) {
        if (!hasPlayed) {
            playAudio(1.0f)
            hasPlayed = true
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        IconButton(onClick = { playAudio(1.0f) }, modifier = Modifier.size(30.dp)) {
            Image(
                painter = painterResource(id = R.drawable.audio),
                contentDescription = "Phát âm"
            )
        }

        IconButton(onClick = { playAudio(0.6f) }, modifier = Modifier.size(30.dp)) {
            Image(
                painter = painterResource(id = R.drawable.slow_audio),
                contentDescription = "Phát chậm"
            )
        }
    }

}