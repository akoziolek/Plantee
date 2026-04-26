package com.example.plantee.ui.viewmodels.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IPlantsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class PlantDetailsEvent {
    object NavigateToDiagnose : PlantDetailsEvent()
    class NavigateToDiagnosis(val diagnosisId: Long) : PlantDetailsEvent()
    class NavigateToRoutine(val routineId: Long) : PlantDetailsEvent()
    class NavigateToEdit(val plantId: Long) : PlantDetailsEvent()
    object NavigateBack : PlantDetailsEvent()
    object PlantDeleted : PlantDetailsEvent()
}

sealed interface PlantDetailsUiState {
    object Loading : PlantDetailsUiState
    data class Success(val plant: Plant) : PlantDetailsUiState
    data class Error(val message: String) : PlantDetailsUiState
    object Deleted : PlantDetailsUiState
}

// https://stackoverflow.com/questions/79763944/how-to-pass-arguments-with-navigation3-using-savedstatehandle
@HiltViewModel(assistedFactory = PlantDetailsViewModel.Factory::class)
class PlantDetailsViewModel @AssistedInject constructor(
    private val plantsRepository: IPlantsRepository,
    @Assisted private val plantId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(plantId: Long): PlantDetailsViewModel
    }

    private val isDeleted = MutableStateFlow(false)

    val state: StateFlow<PlantDetailsUiState> = plantsRepository
        .getPlant(plantId)
        .map { plant ->
            if (isDeleted.value) {
                PlantDetailsUiState.Deleted
            } else if (plant != null) {
                PlantDetailsUiState.Success(plant)
            } else {
                PlantDetailsUiState.Error("Plant not found")
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlantDetailsUiState.Loading
        )

    private val _events = Channel<PlantDetailsEvent>()
    val events = _events.receiveAsFlow()

    fun onDiagnoseClick() {
        viewModelScope.launch {
            _events.send(PlantDetailsEvent.NavigateToDiagnose)
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(PlantDetailsEvent.NavigateBack)
        }
    }

    fun onDiagnosisClick(diagnosisId: Long) {
        viewModelScope.launch {
            _events.send(PlantDetailsEvent.NavigateToDiagnosis(diagnosisId))
        }
    }

    fun onRoutineClick(routineId: Long) {
        viewModelScope.launch {
            _events.send(PlantDetailsEvent.NavigateToRoutine(routineId))
        }
    }

    fun onEditClick() {
        viewModelScope.launch {
            _events.send(PlantDetailsEvent.NavigateToEdit(plantId))
        }
    }

    fun deletePlant() {
        viewModelScope.launch {
            isDeleted.value = true
            plantsRepository.deletePlant(plantId)
            _events.send(PlantDetailsEvent.PlantDeleted)
        }
    }

    fun toggleFavourite() {
        viewModelScope.launch {
            val currentState = state.value
            if(currentState is PlantDetailsUiState.Success) {
                plantsRepository.togglePlantFavourite(plantId)
            }
        }
    }

}
