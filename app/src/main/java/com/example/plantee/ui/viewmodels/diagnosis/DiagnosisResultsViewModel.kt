package com.example.plantee.ui.viewmodels.diagnosis

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IDiagnosesRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.use_cases.SavePlantImageUseCase
import com.example.plantee.ui.nav.DiagnosisInput
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
import java.time.LocalDateTime

sealed class DiagnosisResultsEvent {
    class NavigateToRoutine(val routineId: Long) : DiagnosisResultsEvent()
    class FinishDiagnosis(val diagnosisId: Long) : DiagnosisResultsEvent()
    class ReturnToDiagnose(val input: DiagnosisInput) : DiagnosisResultsEvent()
}

sealed interface DiagnosisResultsUiState {
    object Loading : DiagnosisResultsUiState
    data class Success(
        val diagnosis: Diagnosis,
        val proposedRoutines: List<RoutineSummary>,
        val selectedRoutines: Set<Long> = emptySet(),
        val removeFromAssociatedRoutines: Boolean = false
    ) : DiagnosisResultsUiState
    data class Error(val message: String) : DiagnosisResultsUiState
}

@HiltViewModel(assistedFactory = DiagnosisResultsViewModel.Factory::class)
class DiagnosisResultsViewModel @AssistedInject constructor(
    private val diagnosesRepository: IDiagnosesRepository,
    private val routinesRepository: IRoutinesRepository,
    private val savePlantImageUseCase: SavePlantImageUseCase,
    @Assisted private val input: DiagnosisInput
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(input: DiagnosisInput): DiagnosisResultsViewModel
    }

    private val _state = MutableStateFlow<DiagnosisResultsUiState>(DiagnosisResultsUiState.Loading)
    val state: StateFlow<DiagnosisResultsUiState> = _state.asStateFlow()

    private val _selectedRoutines = MutableStateFlow<Set<Long>>(emptySet())

    private val _events = Channel<DiagnosisResultsEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadMockDiagnosis()
    }

    private fun loadMockDiagnosis() {
        // In a real app, you'd probably call an AI service here.
        // For now, we create a temporary Diagnosis object to show in UI
        val mockDiagnosis = Diagnosis(
            plantId = input.plantId,
            problemDescription = input.problemDescription,
            sunLevel = input.sunLevel.toInt(),
            moistureLevel = input.moistureLevel.toInt(),
            diagnosedAt = LocalDateTime.now(),
            response = "Based on the description and levels, your plant seems to be doing fine but could use more consistent watering.",
            routines = listOf(
                RoutineSummary(1L, "Consistent Watering", "Water every 2 days"),
                RoutineSummary(2L, "Sun Exposure", "Ensure 4 hours of indirect sunlight")
            )
        )
        
        _state.value = DiagnosisResultsUiState.Success(
            diagnosis = mockDiagnosis,
            proposedRoutines = mockDiagnosis.routines,
            selectedRoutines = emptySet()
        )
    }

    fun onRoutineCheckedChange(routineId: Long, checked: Boolean) {
        _selectedRoutines.update { current ->
            val next = if (checked) current + routineId else current - routineId
            if (_state.value is DiagnosisResultsUiState.Success) {
                _state.update { 
                    (it as DiagnosisResultsUiState.Success).copy(selectedRoutines = next)
                }
            }
            next
        }
    }

    fun onRemoveFromRoutinesClick() {
        _state.update {
            if (it is DiagnosisResultsUiState.Success) {
                it.copy(removeFromAssociatedRoutines = !it.removeFromAssociatedRoutines)
            } else it
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(DiagnosisResultsEvent.ReturnToDiagnose(input))
        }
    }

    fun onRoutineClick(routineId: Long) {
        viewModelScope.launch {
            _events.send(DiagnosisResultsEvent.NavigateToRoutine(routineId))
        }
    }

    fun onFinishClick() {
        val currentState = _state.value
        if (currentState !is DiagnosisResultsUiState.Success) return

        viewModelScope.launch {
            if (currentState.removeFromAssociatedRoutines) {
                routinesRepository.removePlantFromAllRoutines(input.plantId)
            }

            val selectedRoutines = currentState.proposedRoutines.filter { 
                _selectedRoutines.value.contains(it.id) 
            }
            
            var mediaList = emptyList<Media>()
            input.imageUri?.let { uriString ->
                val uri = uriString.toUri()
                val savedMedia = savePlantImageUseCase(uri)
                if (savedMedia != null) {
                    mediaList = listOf(savedMedia)
                }
            }

            val diagnosisToSave = currentState.diagnosis.copy(
                routines = selectedRoutines,
                listOfMedia = mediaList
            )
            
            val diagnosisId = diagnosesRepository.createDiagnosis(diagnosisToSave)
            _events.send(DiagnosisResultsEvent.FinishDiagnosis(diagnosisId))
        }
    }
}