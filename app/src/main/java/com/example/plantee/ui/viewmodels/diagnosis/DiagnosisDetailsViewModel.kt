package com.example.plantee.ui.viewmodels.diagnosis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.repositories.IDiagnosesRepository
import com.example.plantee.domain.repositories.IRoutinesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class DiagnosisDetailsEvent {
    object NavigateBack : DiagnosisDetailsEvent()
    class NavigateToRoutine(val routineId: Long) : DiagnosisDetailsEvent()
}

sealed interface DiagnosisDetailsUiState {
    object Loading : DiagnosisDetailsUiState
    data class Success(
        val diagnosis: Diagnosis,
        //TODO RoutineSummary?
        val connectedRoutines: List<Routine>
    ) : DiagnosisDetailsUiState
    data class Error(val message: String) : DiagnosisDetailsUiState
}

@HiltViewModel(assistedFactory = DiagnosisDetailsViewModel.Factory::class)
class DiagnosisDetailsViewModel @AssistedInject constructor(
    private val diagnosesRepository: IDiagnosesRepository,
    private val routinesRepository: IRoutinesRepository,
    @Assisted private val diagnosisId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(diagnosisId: Long): DiagnosisDetailsViewModel
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<DiagnosisDetailsUiState> = diagnosesRepository
        .getDiagnosis(diagnosisId)
        .flatMapLatest { diagnosis ->
            if (diagnosis == null) {
                flowOf(DiagnosisDetailsUiState.Error("Diagnosis not found"))
            } else {
                val routineFlows = diagnosis.routinesIds.map { routinesRepository.getRoutine(it) }
                if (routineFlows.isEmpty()) {
                    flowOf(DiagnosisDetailsUiState.Success(diagnosis, emptyList()))
                } else {
                    combine(routineFlows) { routines ->
                        routines.filterNotNull().toList()
                    }.map { routines ->
                        DiagnosisDetailsUiState.Success(diagnosis, routines)
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DiagnosisDetailsUiState.Loading
        )

    private val _events = Channel<DiagnosisDetailsEvent>()
    val events = _events.receiveAsFlow()

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(DiagnosisDetailsEvent.NavigateBack)
        }
    }

    fun onRoutineClick(routineId: Long) {
        viewModelScope.launch {
            _events.send(DiagnosisDetailsEvent.NavigateToRoutine(routineId))
        }
    }
}
