package com.example.plantee.ui.components.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.plantee.R
import com.example.plantee.utils.rememberSoundEffect
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun ConfettiCelebration() {
    val party = Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        position = Position.Relative(0.5, 0.3),
        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
    )

    KonfettiView(
        modifier = Modifier.fillMaxSize(),
        parties = listOf(party)
    )
}

@Composable
fun CelebrationWrapper(
    isAllDone: Boolean,
    content: @Composable () -> Unit
) {
    var showCelebration by remember { mutableStateOf(false) }
    var wasAlreadyDone by remember { mutableStateOf(isAllDone) }
    val playConfettiSound = rememberSoundEffect(R.raw.confetti)

    LaunchedEffect(isAllDone) {
        if(isAllDone && !wasAlreadyDone) {
            showCelebration = true
            playConfettiSound()
            delay(4000)
            showCelebration = false
        } else if (!isAllDone) {
            showCelebration = false
        }
        wasAlreadyDone = isAllDone
    }

    Box {
        content()
        if(showCelebration) ConfettiCelebration()
    }
}