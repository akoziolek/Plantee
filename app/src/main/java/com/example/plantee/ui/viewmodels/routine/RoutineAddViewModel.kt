package com.example.plantee.ui.viewmodels.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RoutineAddEvent {
    data class NavigateToDetails(val routineId: Long) : RoutineAddEvent()
    object NavigateBack : RoutineAddEvent()
}

data class RoutineAddUiState(
    val name: String = "",
    val nameError: Boolean = false,
    val description: String = "",
    val activeDays: Int = 0,
    val plants: List<PlantSummary> = emptyList(),
    val plantIds: List<Long> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

@HiltViewModel
class RoutineAddViewModel @Inject constructor(
    private val routineRepository: IRoutinesRepository,
    private val plantsRepository: IPlantsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RoutineAddUiState())

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder = _sortOrder.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val state: StateFlow<RoutineAddUiState> = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            plantsRepository.getSearchedPlantsSummaryWithSort(query, SortOrder.NONE).map { filtered ->
                RoutineAddUiState(
                    plants = filtered,
                    isLoading = false
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RoutineAddUiState()
        )

    private val _events = Channel<RoutineAddEvent>()
    val events = _events.receiveAsFlow()

    fun onNameChange(newName: String) {
        _state.update { it.copy(name = newName, nameError = false) }
    }

    fun onDescriptionChange(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    fun onActiveDaysChange(dayToChange: Int) {
        val isChecked = (_state.value.activeDays and (1 shl dayToChange)) != 0
        val newActiveDays = if (isChecked) {
            _state.value.activeDays and (1 shl dayToChange).inv()
        } else {
            _state.value.activeDays or (1 shl dayToChange)
        }

        _state.update { it.copy(activeDays = newActiveDays) }
    }

    fun onPlantClick(plantId: Long) {
        val newPlantsIds: List<Long> = if (_state.value.plantIds.contains(plantId)) {
            _state.value.plantIds.filterNot { it == plantId }
        } else {
            _state.value.plantIds + plantId
        }

        _state.update { it.copy(plantIds = newPlantsIds) }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(RoutineAddEvent.NavigateBack)
        }
    }

    private fun validate(): Boolean {
        val nameIsBlank = _state.value.name.isBlank()
        _state.update { it.copy(nameError = nameIsBlank) }
        return !nameIsBlank
    }

    fun saveRoutine() {
        if (!validate()) return

        viewModelScope.launch {
            val currentState = _state.value
            // FIXME no start date and end date used anywhere
            val routine = Routine(
                name = currentState.name,
                description = currentState.description,
                activeDays = currentState.activeDays,
                plantsIds = currentState.plantIds
            )
            val id = routineRepository.addRoutine(routine)
            resetState()
            _events.send(RoutineAddEvent.NavigateToDetails(routineId = id))
        }
    }

    fun resetState() {
        _state.value = RoutineAddUiState()
        _searchQuery.value = ""
    }
}
