package com.example.capyvocab_fe.core.ui.components

import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
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

enum class PlayingType { NONE, NORMAL, SLOW }

@Composable
fun PronunciationPlayer(
    audioUrl: String,
    wordId: Int,
    autoPlay: Boolean = true
) {
    val context = LocalContext.current
    var hasPlayed by rememberSaveable(wordId) { mutableStateOf(false) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var playingType by remember { mutableStateOf(PlayingType.NONE) }

    fun stopAudio() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
                release()
            }
        }
        mediaPlayer = null
        playingType = PlayingType.NONE
    }

    fun playAudio(type: PlayingType, speed: Float) {
        stopAudio()
        try {
            mediaPlayer = MediaPlayer().apply {
                if (audioUrl.startsWith("content://")) {
                    val uri = Uri.parse(audioUrl)
                    setDataSource(context, uri, null)
                } else {
                    setDataSource(audioUrl)
                }

                setOnPreparedListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        playbackParams = PlaybackParams().setSpeed(speed)
                    }
                    start()
                    playingType = type
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

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    LaunchedEffect(wordId, autoPlay) {
        if (autoPlay && !hasPlayed) {
            playAudio(PlayingType.NORMAL, 1.0f)
            hasPlayed = true
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        IconButton(
            onClick = {
                if (playingType == PlayingType.NORMAL) {
                    stopAudio()
                } else {
                    playAudio(PlayingType.NORMAL, 1.0f)
                }
            },
            modifier = Modifier.size(30.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (playingType == PlayingType.NORMAL) R.drawable.stop_audio else R.drawable.audio
                ),
                contentDescription = "Phát âm"
            )
        }

        IconButton(
            onClick = {
                if (playingType == PlayingType.SLOW) {
                    stopAudio()
                } else {
                    playAudio(PlayingType.SLOW, 0.6f)
                }
            },
            modifier = Modifier.size(30.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (playingType == PlayingType.SLOW) R.drawable.stop_audio else R.drawable.slow_audio
                ),
                contentDescription = "Phát chậm"
            )
        }
    }
}
