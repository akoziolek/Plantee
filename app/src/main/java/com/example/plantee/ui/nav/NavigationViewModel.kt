package com.example.plantee.ui.nav

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
            _backStack.removeAt(_backStack.lastIndex)
        }
    }

    fun popUpTo(target: Screen, inclusive: Boolean = false) {
        val targetIndex = _backStack.indexOfLast { it == target }
        if (targetIndex != -1) {
            val numToKeep = if (inclusive) targetIndex else targetIndex + 1
            while (_backStack.size > numToKeep && _backStack.size > 1) {
                _backStack.removeAt(_backStack.lastIndex)
            }
        }
    }

    fun replace(screen: Screen) {
        if (_backStack.isNotEmpty()) {
            _backStack[_backStack.lastIndex] = screen
        } else {
            _backStack.add(screen)
        }
    }

    fun current(): Screen? = _backStack.lastOrNull()

    fun switchTab(target: Screen) {
        _backStack.clear()
        _backStack.add(target)
    }
}