package com.example.plantee.ui.viewmodels.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.utils.SortOrder
import com.example.plantee.utils.toLocalDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
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
import java.time.LocalDate

sealed class RoutineEditEvent {
    object NavigateBack : RoutineEditEvent()
    object RoutineUpdated : RoutineEditEvent()
}

data class RoutineEditUiState(
    val id: Long = 0L,
    val name: String = "",
    val nameError: Boolean = false,
    val description: String = "",
    val activeDays: Int = 0,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val lastlyDoneAt: LocalDate? = null,
    val availablePlants: List<PlantSummary> = emptyList(),
    val selectedPlants: List<Long> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

@HiltViewModel(assistedFactory = RoutineEditViewModel.Factory::class)
class RoutineEditViewModel @AssistedInject constructor(
    private val routinesRepository: IRoutinesRepository,
    private val plantsRepository: IPlantsRepository,
    @Assisted private val routineId: Long
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(routineId: Long): RoutineEditViewModel
    }

    private val _state = MutableStateFlow(RoutineEditUiState())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _events = Channel<RoutineEditEvent>()
    val events = _events.receiveAsFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _filteredPlants = _searchQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            plantsRepository.getSearchedPlantsSummaryWithSort(query, SortOrder.NONE)
        }

    val state: StateFlow<RoutineEditUiState> = combine(_state, _filteredPlants) { currentState, filteredPlants ->
        currentState.copy(
            availablePlants = filteredPlants,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RoutineEditUiState()
    )

    init {
        loadRoutine()
    }

    private fun loadRoutine() {
        viewModelScope.launch {
            routinesRepository.getRoutine(routineId).collect { routine ->
                routine?.let { r ->
                    _state.update {
                        it.copy(
                            id = r.id,
                            name = r.name,
                            description = r.description ?: "",
                            activeDays = r.activeDays ?: 0,
                            startDate = r.startDate,
                            endDate = r.endDate,
                            lastlyDoneAt = r.lastlyDoneAt,
                            selectedPlants = r.plants.map { plant -> plant.id },
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

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

    fun onDateRangeSelected(start: Long?, end: Long?) {
        _state.update { it.copy(
            startDate = start?.toLocalDate(),
            endDate = end?.toLocalDate()
        ) }
    }

    fun onPlantClick(plantId: Long) {
        _state.update { currentState ->
            val newSelectedIds = if (currentState.selectedPlants.contains(plantId)) {
                currentState.selectedPlants - plantId
            } else {
                currentState.selectedPlants + plantId
            }
            currentState.copy(selectedPlants = newSelectedIds)
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(RoutineEditEvent.NavigateBack)
        }
    }

    private fun validate(): Boolean {
        val nameIsBlank = _state.value.name.isBlank()
        _state.update { it.copy(nameError = nameIsBlank) }
        return !nameIsBlank
    }

    fun updateRoutine() {
        if (!validate()) return

        viewModelScope.launch {
            val currentState = state.value
            val selectedPlantSummaries = currentState.availablePlants.filter {
                currentState.selectedPlants.contains(it.id)
            }
            val routine = Routine(
                id = currentState.id,
                name = currentState.name,
                description = currentState.description,
                activeDays = currentState.activeDays,
                startDate = currentState.startDate,
                endDate = currentState.endDate,
                lastlyDoneAt = currentState.lastlyDoneAt,
                plants = selectedPlantSummaries
            )
            routinesRepository.updateRoutine(routine)
            _events.send(RoutineEditEvent.RoutineUpdated)
        }
    }
}
