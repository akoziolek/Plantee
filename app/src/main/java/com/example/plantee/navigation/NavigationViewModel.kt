package com.example.plantee.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

val topLevelScreens = setOf(
    Screen.Home::class,
    Screen.Plants::class,
    Screen.PlantDetails::class,
    Screen.Routines::class,
    Screen.RoutineDetails::class
)

class NavigationViewModel : ViewModel() {

    private val _backStack = mutableStateListOf<Screen>(Screen.Home)
    val backStack: List<Screen> = _backStack
    val isBottomBarVisible: Boolean
        get() = _backStack.lastOrNull()?.let { current ->
            topLevelScreens.contains(current::class)
        } ?: false

    fun navigate(screen: Screen) {
        _backStack.add(screen)
    }

    fun back() {
        if (_backStack.size > 1) {
            // istnieje też _backStack.removeLast(), ale nie jest obslugiwane na wersjach < 35
            _backStack.removeAt(_backStack.lastIndex)
        }
    }

    fun popUpTo(target: Screen, inclusive: Boolean = false) {
        while (_backStack.isNotEmpty() && _backStack.last() != target) {
            _backStack.removeAt(_backStack.lastIndex)
        }
        if (inclusive && _backStack.isNotEmpty()) {
            _backStack.removeAt(_backStack.lastIndex)
        }
    }

    fun replace(screen: Screen) {
        _backStack.removeLastOrNull()
        _backStack.add(screen)
    }

    fun current(): Screen? = _backStack.lastOrNull()

    fun switchTab(target: Screen) {
        _backStack.clear()
        _backStack.add(target)
    }
}