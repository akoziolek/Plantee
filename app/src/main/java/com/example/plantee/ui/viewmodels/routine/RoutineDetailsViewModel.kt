package com.example.plantee.ui.viewmodels.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class RoutineDetailsEvent {
    class NavigateToPlant(val plantId: Long) : RoutineDetailsEvent()
    class NavigateToEdit(val routineId: Long) : RoutineDetailsEvent()
    object NavigateBack : RoutineDetailsEvent()
    object RoutineDeleted : RoutineDetailsEvent()
}

sealed interface RoutineDetailsUiState {
    object Loading : RoutineDetailsUiState
    data class Success(
        val routine: Routine,
        val connectedPlants: List<PlantSummary>
    ) : RoutineDetailsUiState
    data class Error(val message: String) : RoutineDetailsUiState
    object Deleted : RoutineDetailsUiState
}

@HiltViewModel(assistedFactory = RoutineDetailsViewModel.Factory::class)
class RoutineDetailsViewModel @AssistedInject constructor(
    private val routinesRepository: IRoutinesRepository,
    private val plantsRepository: IPlantsRepository,
    @Assisted private val routineId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(routineId: Long): RoutineDetailsViewModel
    }

    private val isDeleted = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<RoutineDetailsUiState> = routinesRepository
        .getRoutine(routineId)
        .flatMapLatest { routine ->
            when {
                isDeleted.value -> flowOf(RoutineDetailsUiState.Deleted)

                routine != null -> {
                    val plantFlows = routine.plantsIds.map { plantsRepository.getPlantSummary(it) }

                    if (plantFlows.isEmpty()) {
                        flowOf(RoutineDetailsUiState.Success(routine, emptyList()))
                    } else {
                        combine(plantFlows) { plants ->
                            RoutineDetailsUiState.Success(
                                routine = routine,
                                connectedPlants = plants.filterNotNull()
                            )
                        }
                    }
                }

                else -> flowOf(RoutineDetailsUiState.Error("Routine not found"))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RoutineDetailsUiState.Loading
        )

    private val _events = Channel<RoutineDetailsEvent>()
    val events = _events.receiveAsFlow()

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(RoutineDetailsEvent.NavigateBack)
        }
    }

    fun onPlantClick(plantId: Long) {
        viewModelScope.launch {
            _events.send(RoutineDetailsEvent.NavigateToPlant(plantId))
        }
    }

    fun onEditClick() {
        viewModelScope.launch {
            _events.send(RoutineDetailsEvent.NavigateToEdit(routineId))
        }
    }

    fun deleteRoutine() {
        viewModelScope.launch {
            isDeleted.value = true
            routinesRepository.deleteRoutine(routineId)
            _events.send(RoutineDetailsEvent.RoutineDeleted)
        }
    }


}