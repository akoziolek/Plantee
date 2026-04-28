package com.example.plantee.ui.viewmodels.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    val lastlyDoneAt: LocalDate? = null,
    val plantIds: List<Long> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel(assistedFactory = RoutineEditViewModel.Factory::class)
class RoutineEditViewModel @AssistedInject constructor(
    private val routinesRepository: IRoutinesRepository,
    private val plantsRepository: IPlantsRepository,
    @Assisted private val routineId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(plantId: Long): RoutineEditViewModel
    }

    private val _state = MutableStateFlow(RoutineEditUiState())
    val state: StateFlow<RoutineEditUiState> = _state.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _events = Channel<RoutineEditEvent>()
    val events = _events.receiveAsFlow()

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
                            lastlyDoneAt = r.lastlyDoneAt,
                            plantIds = r.plantsIds,
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

    fun onPlantIdsChange(newPlantIds: List<Long>) {
        _state.update { it.copy(plantIds = newPlantIds) }
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
            val currentState = _state.value
            val routine = Routine(
                id = currentState.id,
                name = currentState.name,
                description = currentState.description,
                activeDays = currentState.activeDays,
                lastlyDoneAt = currentState.lastlyDoneAt,

            )
            routinesRepository.updateRoutine(routine)
            // TODO update plants for routine
            _events.send(RoutineEditEvent.RoutineUpdated)
        }
    }
}