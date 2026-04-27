package com.example.plantee.ui.viewmodels.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IRoutinesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    val plantIds: List<Long> = emptyList()
)

@HiltViewModel
class RoutineAddViewModel @Inject constructor(
    private val routineRepository: IRoutinesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(RoutineAddUiState())
    val state: StateFlow<RoutineAddUiState> = _state.asStateFlow()

    private val _events = Channel<RoutineAddEvent>()
    val events = _events.receiveAsFlow()

    fun onNameChange(newName: String) {
        _state.update { it.copy(name = newName, nameError = false) }
    }

    fun onDescriptionChange(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    fun onActiveDaysChange(newActiveDays: Int) {
        _state.update { it.copy(activeDays = newActiveDays) }
    }

    fun onPlantsChange(newPlantsIds: List<Long>) {
        _state.update { it.copy(plantIds = newPlantsIds) }
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
            // TODO no start date and end date used anywhere
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
    }
}