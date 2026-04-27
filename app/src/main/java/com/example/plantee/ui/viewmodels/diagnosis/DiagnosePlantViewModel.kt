package com.example.plantee.ui.viewmodels.diagnosis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.repositories.IDiagnosesRepository
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

sealed class DiagnosePlantEvent {
    class NavigateToDiagnosis(val diagnosisId: Long) : DiagnosePlantEvent()
    object NavigateBack : DiagnosePlantEvent()
}

// TODO image
data class DiagnosePlantUiState(
    val plantId: Long = 0L,
    val moistureLevel: Float = 0.7f,
    val sunLevel: Float = 0.3f,
    val problemDescription: String = "",
    val problemDescriptionError: Boolean = false
)

@HiltViewModel(assistedFactory = DiagnosePlantViewModel.Factory::class)
class DiagnosePlantViewModel @AssistedInject constructor(
    private val diagnosesRepository: IDiagnosesRepository,
    @Assisted private val plantId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(plantId: Long): DiagnosePlantViewModel
    }

    private val _state = MutableStateFlow(DiagnosePlantUiState(plantId = plantId))
    val state: StateFlow<DiagnosePlantUiState> = _state.asStateFlow()

    private val _events = Channel<DiagnosePlantEvent>()
    val events = _events.receiveAsFlow()

    fun onMoistureLevelChange(newMoistureLevel: Float) {
        _state.update { it.copy(moistureLevel = newMoistureLevel) }
    }

    fun onSunLevelChange(newSunLevel: Float) {
        _state.update { it.copy(sunLevel = newSunLevel) }
    }

    fun onProblemDescriptionChange(newDescription: String) {
        _state.update { it.copy(problemDescription = newDescription, problemDescriptionError = false) }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(DiagnosePlantEvent.NavigateBack)
        }
    }

    private fun validate(): Boolean {
        // TODO do we validate picture presence?
        val descriptionIsBlank = _state.value.problemDescription.isBlank()
        _state.update { it.copy(problemDescriptionError = descriptionIsBlank) }
        return !descriptionIsBlank
    }

    fun onDiagnoseClick() {
        if(!validate()) return
        // TODO tu akurat chce sie upewnic, ze tak ma dzialc ten proces, DLA UWAGI KOMENTARZ PO POLSKU
        viewModelScope.launch {
            val currentState = _state.value
            val diagnosis = Diagnosis(
                plantId = currentState.plantId,
                problemDescription = currentState.problemDescription,
                moistureLevel = currentState.moistureLevel.toInt(),
                sunLevel = currentState.sunLevel.toInt(),
                diagnosedAt = LocalDateTime.now()
            )
            val diagnosisId = diagnosesRepository.createDiagnosis(diagnosis)
            // TODO przekazywanie calego obiektu do diagnozy, do viewmodelu diagnose tez trzeba wtedy dodac diagnoze
            // TODO usunac navbar z detali
            _events.send(DiagnosePlantEvent.NavigateToDiagnosis(diagnosisId))
        }
    }
}