package com.example.plantee.ui.viewmodels.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.repositories.IRoutinesStatisticsRepository
import com.example.plantee.domain.repositories.IUserPreferencesRepository
import com.example.plantee.domain.repositories.ISettingsRepository
import com.example.plantee.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

sealed class HomeEvent {
    data class NavigateToPlant(val plantId: Long) : HomeEvent()
    data class NavigateToRoutine(val routineId: Long) : HomeEvent()
    object NavigateToRoutines : HomeEvent()
    object NavigateToPlantAdd : HomeEvent()
    object RequestNotificationPermission : HomeEvent()
}

data class HomeUiState(
    val plants: List<PlantSummary> = emptyList(),
    val todayRoutines: List<Routine> = emptyList(),
    val isLoading: Boolean = true,
    val streakProgress: Float? = null,
    val streakDays: Int? = null,
    val isNotificationsEnabled: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val routinesRepository: IRoutinesRepository,
    private val userPreferencesRepository: IUserPreferencesRepository,
    private val routinesStatisticsRepository: IRoutinesStatisticsRepository,
    private val settingsRepository: ISettingsRepository
) : ViewModel() {
    private val _currentDay = MutableStateFlow<DayOfWeek>(LocalDate.now().dayOfWeek)
    val currentDay = _currentDay.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(false)


    val isDarkTheme = userPreferencesRepository.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            routinesStatisticsRepository.syncStreak()
            while (true) {
                val now = LocalDate.now().dayOfWeek
                if (_currentDay.value != now) {
                    _currentDay.value = now
                }
                delay(60_000)
            }
        }
    }

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder = _sortOrder.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val plantsFlow = _sortOrder
        .flatMapLatest { sort ->
            plantsRepository.getSearchedPlantsSummaryWithSort("", sort)
        }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val todayRoutinesFlow = _currentDay.flatMapLatest { day ->
        routinesRepository.getRoutinesForWeekdaySummary(day.value)
    }

    val state: StateFlow<HomeUiState> = combine(
        plantsFlow,
        todayRoutinesFlow,
        settingsRepository.getNotificationsEnabled(),
        routinesStatisticsRepository.getEffectiveStreak()
    ) { sortResults, todayRoutines, notificationsEnabled, currentStreak ->
        val totalRoutines = todayRoutines.size
        val completedRoutines = todayRoutines.count { it.lastlyDoneAt == LocalDate.now() }

        val progress = if (totalRoutines > 0) {
            completedRoutines.toFloat() / totalRoutines
        } else {
            null
        }

        HomeUiState(
            plants = sortResults,
            todayRoutines = todayRoutines,
            isLoading = false,
            isNotificationsEnabled = notificationsEnabled,
            streakProgress = progress,
            streakDays = currentStreak
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )


    fun toggleSortOrder() {
        _sortOrder.value = _sortOrder.value.next()
    }

    fun onPlantClick(plantId: Long) {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToPlant(plantId))
        }
    }

    fun onRoutineClick(routineId: Long) {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToRoutine(routineId))
        }
    }

    fun onRoutinesClick() {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToRoutines)
        }
    }

    fun onCheckboxClick(routineId: Long) {
        viewModelScope.launch {
            val routine = state.value.todayRoutines.find { it.id == routineId }
            val today = LocalDate.now()
            val newDate = if (routine?.lastlyDoneAt == today) null else today

            routinesRepository.toggleRoutineDone(routineId, newDate)
        }
    }

    fun onAddPlantClick() {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToPlantAdd)
        }
    }

    fun onPlantBookmarkClick(plantId: Long) {
        viewModelScope.launch {
            plantsRepository.togglePlantFavourite(plantId)
        }
    }

    fun toggleTheme(currentlyDark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.setDarkTheme(!currentlyDark)
        }
    }

    // FIXME screen blink
    fun changeLanguage(langCode: String) {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (currentLocales.toLanguageTags() == langCode) {
            return
        }

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun onNotificationIconClick(hasPermission: Boolean) {
        Log.d("HomeViewModel", "Icon clicked. hasPermission: $hasPermission")
        if (!hasPermission) {
            viewModelScope.launch {
                _events.send(HomeEvent.RequestNotificationPermission)
            }
        } else {
            viewModelScope.launch {
                val current = settingsRepository.getNotificationsEnabled().first()
                settingsRepository.setNotificationsEnabled(!current)
                Log.d("HomeViewModel", "Notifications toggled to: ${!current}")
            }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Explicitly setting notifications enabled: $enabled")
            settingsRepository.setNotificationsEnabled(enabled)
        }
    }
}
