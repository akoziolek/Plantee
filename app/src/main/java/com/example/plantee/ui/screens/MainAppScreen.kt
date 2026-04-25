package com.example.plantee.ui.screens

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.plantee.ui.nav.MainNavigation
import com.example.plantee.ui.nav.NavigationViewModel
import com.example.plantee.ui.components.base.NavBar

@Composable
fun MainAppScreen() {
    val navigationViewModel: NavigationViewModel = viewModel()

    Scaffold(
       bottomBar  = {
            if (navigationViewModel.isBottomBarVisible) {
                NavBar(
                    currentScreen = navigationViewModel.current(),
                    onTabSelected = { screen ->
                        navigationViewModel.switchTab(target = screen)
                    }
                )
            }
        }
    ) { paddingValues ->
        MainNavigation(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues),
            viewModel = navigationViewModel
        )
    }
}