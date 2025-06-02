package com.example.capyvocab_fe.core.ui.components

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    wordId: Int,
    autoPlay: Boolean = true
) {
    val context = LocalContext.current
    var hasPlayed by rememberSaveable(wordId) { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
        mediaPlayer = null
        isPlaying = false
    }

    fun playAudio(speed: Float) {
        stopAudio() // Stop any currently playing audio
        try {
            mediaPlayer = MediaPlayer().apply {
                if (audioUrl.startsWith("content://")) {
                    val uri = android.net.Uri.parse(audioUrl)
                    setDataSource(context, uri, null)
                } else {
                    setDataSource(audioUrl)
                }
                setOnPreparedListener {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        playbackParams = playbackParams.setSpeed(speed)
                    }
                    start()
                    isPlaying = true
                }
                setOnCompletionListener {
                    stopAudio()
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            stopAudio()
        }
    }
    // Cleanup when component is disposed
    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    // Tự động phát tốc độ thường khi hiển thị
    LaunchedEffect(wordId, autoPlay) {
        if (autoPlay && !hasPlayed) {
            playAudio(1.0f)
            hasPlayed = true
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        IconButton(
            onClick = {
                if (isPlaying) {
                    stopAudio()
                } else {
                    playAudio(1.0f)
                }
            },
            modifier = Modifier.size(30.dp)
        ) {
            Image(
                painter = painterResource(id = if (isPlaying) R.drawable.stop_audio else R.drawable.audio),
                contentDescription = "Phát âm"
            )
        }

        IconButton(onClick = {
            if (isPlaying) {
                stopAudio()
            }
            playAudio(0.6f)
        }, modifier = Modifier.size(30.dp)) {
            Image(
                painter = painterResource(id = R.drawable.slow_audio),
                contentDescription = "Phát chậm"
            )
        }
    }
}