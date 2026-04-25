package com.example.plantee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.plantee.ui.screens.MainAppScreen
import com.example.plantee.ui.theme.PlanteeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            //  eventual blocking of the splash view for the time of loading stuff
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlanteeTheme {
                MainAppScreen()
            }
        }
    }
}
