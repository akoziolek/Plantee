package com.example.plantee.ui.viewmodels.home

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.repositories.IRoutinesStatisticsRepository
import com.example.plantee.domain.repositories.ISettingsRepository
import com.example.plantee.domain.repositories.IUserPreferencesRepository
import com.example.plantee.utils.AppTheme
import com.example.plantee.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
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
    val todayRoutines: List<RoutineSummary> = emptyList(),
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
    private val _currentDay = MutableStateFlow<LocalDate>(LocalDate.now())
    val currentDay: StateFlow<LocalDate> = _currentDay.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder = _sortOrder.asStateFlow()

    private var midnightJob: Job? = null

    init {
        startMidnightTimer()
    }

    fun onResume() {
        val today = LocalDate.now()
        if (_currentDay.value != today) {
            _currentDay.value = today
            syncStreak()

            startMidnightTimer()
        }
    }

    private fun startMidnightTimer() {
        midnightJob?.cancel()
        midnightJob = viewModelScope.launch {
            while (isActive) {
                val now = LocalDateTime.now()
                val nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay()

                delay(Duration.between(now, nextMidnight).toMillis())

                _currentDay.value = LocalDate.now()
                syncStreak()
            }
        }
    }

    val theme = userPreferencesRepository.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<HomeUiState> = combine(currentDay, _sortOrder) { day, sort ->
        Pair(day, sort)
    }.flatMapLatest { (day, sort) ->
        combine(
            plantsRepository.getSearchedPlantsSummaryWithSort("", sort),
            routinesRepository.getRoutinesForDay(day),
            settingsRepository.getNotificationsEnabled(),
            routinesStatisticsRepository.getEffectiveStreak(day)
        ) { sortResults, todayRoutines, notificationsEnabled, currentStreak ->
            val totalRoutines = todayRoutines.size
            val completedRoutines = todayRoutines.count { it.lastlyDoneAt == day }

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
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun syncStreak() {
        viewModelScope.launch(Dispatchers.IO) {
            routinesStatisticsRepository.syncStreak()
        }
    }

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
            val today = currentDay.value
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

    fun toggleTheme() {
        viewModelScope.launch {
            val nextTheme = when (theme.value) {
                AppTheme.LIGHT -> AppTheme.DARK
                AppTheme.DARK -> AppTheme.SYSTEM
                AppTheme.SYSTEM -> AppTheme.LIGHT
            }
            userPreferencesRepository.setTheme(nextTheme)
        }
    }

    fun changeLanguage(langCode: String) {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (currentLocales.toLanguageTags() == langCode) {
            return
        }

        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(langCode)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    fun onNotificationIconClick(hasPermission: Boolean) {
        if (!hasPermission) {
            viewModelScope.launch {
                _events.send(HomeEvent.RequestNotificationPermission)
            }
        } else {
            viewModelScope.launch {
                val current = settingsRepository.getNotificationsEnabled().first()
                settingsRepository.setNotificationsEnabled(!current)
            }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
        }
    }

    fun checkIfAllRoutinesAreDone(): Boolean {
        val currentState = state.value
        return currentState.todayRoutines.isNotEmpty() && currentState.todayRoutines.all {
            it.lastlyDoneAt == currentDay.value
        }
    }
}
