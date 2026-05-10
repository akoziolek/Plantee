package com.example.plantee.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.repositories.IUserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface ThemeState {
    object Loading : ThemeState
    data class Success(val isDark: Boolean?) : ThemeState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferencesRepository: IUserPreferencesRepository
) : ViewModel() {

    val themeState: StateFlow<ThemeState> = userPreferencesRepository.isDarkTheme
        .map { ThemeState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ThemeState.Loading
        )
}
