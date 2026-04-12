package com.example.plantee.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.plantee.navigation.MainNavigation
import com.example.plantee.navigation.Screen
import com.example.plantee.ui.components.base.NavBar

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()

    // Obserwujemy gdzie jesteśmy
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Definiujemy, na których ekranach pasek ma być WIDOCZNY
    val bottomBarScreens = listOf(
        Screen.Home::class,
        Screen.Plants::class,
        Screen.PlantDetails::class,
        Screen.Routines::class,
        Screen.RoutineDetails::class
    )

    // Logika pokazująca pasek
    val showBottomBar = currentDestination?.hierarchy?.any { dest ->
        bottomBarScreens.any { screenClass -> dest.hasRoute(screenClass) }
    } == true

    Scaffold(
        bottomBar = {
            // Pasek rysuje się tylko, gdy warunek jest spełniony
            if (showBottomBar) {
                NavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        MainNavigation(
            navController = navController
        )
    }
}