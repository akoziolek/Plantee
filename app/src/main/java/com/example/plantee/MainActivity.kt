package com.example.plantee

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.plantee.data.notifications.NotificationScheduler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.ui.MainViewModel
import com.example.plantee.ui.ThemeState
import com.example.plantee.ui.screens.MainAppScreen
import com.example.plantee.ui.theme.PlanteeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.themeState.value is ThemeState.Loading
        }

        enableEdgeToEdge()
        setContent {
            val state by viewModel.themeState.collectAsStateWithLifecycle()
            if (state is ThemeState.Success) {
                val isDarkThemePref = (state as ThemeState.Success).isDark
                val darkTheme = isDarkThemePref ?: isSystemInDarkTheme()

                PlanteeTheme(darkTheme = darkTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        MainAppScreen()
                    }
                }
            }
        }

        NotificationScheduler.scheduleDailyReminder(
            this,
            NotificationScheduler.MORNING_HOUR,
            0,
            NotificationScheduler.TAG_MORNING
        )
        NotificationScheduler.scheduleDailyReminder(
            this,
            NotificationScheduler.EVENING_HOUR,
            0,
            NotificationScheduler.TAG_EVENING
        )
    }
}
