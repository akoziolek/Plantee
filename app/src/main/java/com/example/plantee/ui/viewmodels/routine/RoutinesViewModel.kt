package com.example.plantee.ui.viewmodels.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.utils.DayBitmaskHelper
import com.example.plantee.utils.RoutineStatus
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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

sealed class RoutinesEvent {
    data class NavigateToDetails(val routineId: Long) : RoutinesEvent()
    object NavigateToAdd : RoutinesEvent()
    object NavigateBack : RoutinesEvent()
}

data class RoutinesUiState(
    val routines: List<RoutineSummary> = emptyList(),
    val todayRoutines: List<Routine> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

data class FilterState(
    val status: RoutineStatus = RoutineStatus.Active,
    val selectedDays: Int = DayBitmaskHelper.allDaysMask()
)

@HiltViewModel
class RoutinesViewModel @Inject constructor(
    private val routinesRepository: IRoutinesRepository
) : ViewModel() {
    private val _currentDay = MutableStateFlow<DayOfWeek>(LocalDate.now().dayOfWeek)
    val currentDay = _currentDay.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                val now = LocalDate.now().dayOfWeek
                if (_currentDay.value != now) {
                    _currentDay.value = now
                }
                delay(60_000)
            }
        }
    }

    private val _events = Channel<RoutinesEvent>()
    val events = _events.receiveAsFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder = _sortOrder.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filterState = MutableStateFlow(FilterState())
    val filterState = _filterState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val searchFlow = combine(
        _searchQuery.debounce(300L).distinctUntilChanged(),
        _sortOrder,
        _filterState
    ) { query, sort, filter -> Triple(query, sort, filter) }
        .flatMapLatest { (query, sort, filter) ->
            routinesRepository.getSearchedRoutinesWithSortAndFilterSummary(query, sort, filter) }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val todayFlow = _currentDay.flatMapLatest { day ->
        routinesRepository.getRoutinesForWeekdaySummary(day.value)
    }

    val state: StateFlow<RoutinesUiState> = combine(
        searchFlow,
        todayFlow
    ) { searchResults, todayRoutines ->
        RoutinesUiState(
            routines = searchResults,
            todayRoutines = todayRoutines,
            isLoading = false
        )
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RoutinesUiState()
    )

    fun updateFilterStatus(status: RoutineStatus) {
        _filterState.update { it.copy(status = status) }
    }

    fun toggleFilterDay(day: DayOfWeek) {
        _filterState.update { current ->
            val isCurrentDaySelected = DayBitmaskHelper.isSelected(current.selectedDays, day)
            val selectedCount = DayBitmaskHelper.selectedDaysCount(current.selectedDays)

            if (isCurrentDaySelected && selectedCount <= 1) {
                current
            } else {
                current.copy(selectedDays = DayBitmaskHelper.toggleBit(current.selectedDays, day))
            }
        }
    }

    fun selectAllDays() {
        _filterState.update { it.copy(selectedDays = DayBitmaskHelper.allDaysMask()) }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSortOrder() {
        _sortOrder.value = _sortOrder.value.next()
    }

    fun onRoutineClick(routineId: Long) {
        viewModelScope.launch {
            _events.send(RoutinesEvent.NavigateToDetails(routineId))
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(RoutinesEvent.NavigateBack)
        }
    }

    fun onAddClick() {
        viewModelScope.launch {
            _events.send(RoutinesEvent.NavigateToAdd)
        }
    }

    fun onCheckboxClick(routineId: Long) {
        val routine = state.value.todayRoutines.find { it.id == routineId }

        var date = LocalDate.now()

        if (routine?.lastlyDoneAt == date) {
            date = null
        }

        viewModelScope.launch {
            // TODO Why unused variable?
            val currentState = state.value
            routinesRepository.toggleRoutineDone(routineId, date)
        }
    }
}