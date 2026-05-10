package com.example.plantee.ui.viewmodels.diagnosis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.RoutineSummary
import com.example.plantee.domain.repositories.IDiagnosesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class DiagnosisResultsEvent {
    object NavigateBack : DiagnosisResultsEvent()
    class NavigateToRoutine(val routineId: Long) : DiagnosisResultsEvent()
    class FinishDiagnosis(val diagnosisId: Long) : DiagnosisResultsEvent()
}

sealed interface DiagnosisResultsUiState {
    object Loading : DiagnosisResultsUiState
    data class Success(
        val diagnosis: Diagnosis,
        val proposedRoutines: List<RoutineSummary>,
        val selectedRoutines: Set<Long> = emptySet()
    ) : DiagnosisResultsUiState
    data class Error(val message: String) : DiagnosisResultsUiState
}

@HiltViewModel(assistedFactory = DiagnosisResultsViewModel.Factory::class)
class DiagnosisResultsViewModel @AssistedInject constructor(
    private val diagnosesRepository: IDiagnosesRepository,
    @Assisted private val diagnosisId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(diagnosisId: Long): DiagnosisResultsViewModel
    }

    private val _selectedRoutines = MutableStateFlow<Set<Long>>(emptySet())

    val state: StateFlow<DiagnosisResultsUiState> = diagnosesRepository
        .getDiagnosis(diagnosisId)
        .combine(_selectedRoutines) { diagnosis, selected ->
            if (diagnosis == null) {
                DiagnosisResultsUiState.Error("Diagnosis not found")
            } else {
                DiagnosisResultsUiState.Success(
                    diagnosis = diagnosis,
                    proposedRoutines = diagnosis.routines,
                    selectedRoutines = selected
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DiagnosisResultsUiState.Loading
        )

    private val _events = Channel<DiagnosisResultsEvent>()
    val events = _events.receiveAsFlow()

    fun onRoutineCheckedChange(routineId: Long, checked: Boolean) {
        _selectedRoutines.update { current ->
            if (checked) current + routineId else current - routineId
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(DiagnosisResultsEvent.NavigateBack)
        }
    }

    fun onRoutineClick(routineId: Long) {
        viewModelScope.launch {
            _events.send(DiagnosisResultsEvent.NavigateToRoutine(routineId))
        }
    }

    fun onFinishClick() {
        viewModelScope.launch {
            // TODO: Here you'd typically save the selected routines before finishing
            _events.send(DiagnosisResultsEvent.FinishDiagnosis(diagnosisId))
        }
    }
}
