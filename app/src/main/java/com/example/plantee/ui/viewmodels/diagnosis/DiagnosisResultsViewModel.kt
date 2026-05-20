package com.example.plantee.ui.viewmodels.diagnosis

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IDiagnosesRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import com.example.plantee.domain.use_cases.AIDiagnoseUseCase
import com.example.plantee.domain.model.AiDiagnosisResult
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
import java.time.LocalDate
import java.time.LocalDateTime

sealed class DiagnosisResultsEvent {
    class NavigateToRoutine(val routineId: Long) : DiagnosisResultsEvent()
    class FinishDiagnosis(val diagnosisId: Long) : DiagnosisResultsEvent()
    class ReturnToDiagnose(val input: DiagnosisInput) : DiagnosisResultsEvent()
    object Close : DiagnosisResultsEvent()
}

sealed interface DiagnosisResultsUiState {
    object Loading : DiagnosisResultsUiState
    data class Success(
        val aiDiagnosisResult: AiDiagnosisResult,
        val selectedRoutines: Set<Long> = emptySet(),
        val removeFromAssociatedRoutines: Boolean = false,
        val diagnosedAt: LocalDateTime = LocalDateTime.now()
    ) : DiagnosisResultsUiState
    data class Error(val message: String) : DiagnosisResultsUiState
}

@HiltViewModel(assistedFactory = DiagnosisResultsViewModel.Factory::class)
class DiagnosisResultsViewModel @AssistedInject constructor(
    private val aiDiagnoseUseCase: AIDiagnoseUseCase,
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

    private val _events = Channel<DiagnosisResultsEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.value = DiagnosisResultsUiState.Loading
            try {
                val response = aiDiagnoseUseCase(input)
                if (response != null) {
                    if(response.isPlantRelated) {
                        _state.value = DiagnosisResultsUiState.Success(
                            aiDiagnosisResult = response,
                            diagnosedAt = LocalDateTime.now()
                        )
                    } else {
                        _state.value = DiagnosisResultsUiState.Error(response.diagnosisDescription)
                    }
                } else {
                    _state.value = DiagnosisResultsUiState.Error("Could not generate diagnosis. Please try again later.")
                }
            } catch (e: Exception) {
                val errorMsg = e.message ?: "An unexpected error occurred"
                _state.value = DiagnosisResultsUiState.Error(errorMsg)
            }
        }
    }

    fun onRoutineCheckedChange(routineId: Long, checked: Boolean) {
        _state.update { currentState ->
            if (currentState is DiagnosisResultsUiState.Success) {
                val currentRoutines = currentState.selectedRoutines
                val nextRoutines = if (checked) {
                    currentRoutines + routineId
                } else {
                    currentRoutines - routineId
                }

                currentState.copy(selectedRoutines = nextRoutines)
            } else {
                currentState
            }
        }
    }

    fun onRemoveFromRoutinesClick() {
        val currentState = _state.value
        if (currentState is DiagnosisResultsUiState.Success) {
            _state.value = currentState.copy(removeFromAssociatedRoutines = !currentState.removeFromAssociatedRoutines)
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
        if (currentState is DiagnosisResultsUiState.Error) {
            viewModelScope.launch {
                _events.send(DiagnosisResultsEvent.Close)
            }
            return
        }
        if (currentState !is DiagnosisResultsUiState.Success) return

        val routinesToSave = currentState.aiDiagnosisResult.proposedRoutines
            .filter { currentState.selectedRoutines.contains(it.tempId) }
            .map { proposed ->
                val startDate = runCatching { LocalDate.parse(proposed.startDate) }.getOrDefault(LocalDate.now())
                val endDate = runCatching { LocalDate.parse(proposed.endDate) }.getOrNull()

                Routine(
                    name = proposed.name,
                    description = proposed.description,
                    startDate = startDate,
                    endDate = endDate,
                    activeDays = proposed.activeDays
                )
            }

        viewModelScope.launch {
            // TODO - transaction? or delete id association from plant than perform this, than update? or update than delete
            val savedMedia: Media? = input.imageUri?.let { uriString ->
                savePlantImageUseCase(uriString.toUri())
            }

            if (currentState.removeFromAssociatedRoutines) {
                routinesRepository.removePlantFromAllRoutines(input.plantId)
            }

            val diagnosisId = diagnosesRepository.createDiagnosis(
                diagnosis = Diagnosis(
                    plantId = input.plantId,
                    problemDescription = input.problemDescription,
                    sunLevel = input.sunLevel,
                    moistureLevel = input.moistureLevel,
                    diagnosedAt = currentState.diagnosedAt,
                    response = currentState.aiDiagnosisResult.diagnosisDescription,
                    media = savedMedia
                ),
                routines = routinesToSave
            )

            _events.send(DiagnosisResultsEvent.FinishDiagnosis(diagnosisId))
        }
    }
}