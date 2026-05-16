package com.example.plantee.utils

import android.media.SoundPool
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberSoundEffect(
    @androidx.annotation.RawRes resId: Int
): () -> Unit {
    val context = LocalContext.current
    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .build()
    }
    var isLoaded by remember { mutableStateOf(false) }

    var soundId by remember { mutableIntStateOf(0) }
    LaunchedEffect(resId) {
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isLoaded = true
            }
        }
        soundId = soundPool.load(context, resId, 1)
    }

    DisposableEffect(soundPool) {
        onDispose {
            soundPool.release()
        }
    }

    return remember(soundId, soundPool, isLoaded) {
        {
            if (soundId != 0 && isLoaded) {
                soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
            }
        }
    }
}