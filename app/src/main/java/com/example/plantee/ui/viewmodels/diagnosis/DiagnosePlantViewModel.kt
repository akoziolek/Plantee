package com.example.plantee.ui.viewmodels.diagnosis

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import androidx.core.net.toUri

sealed class DiagnosePlantEvent {
    class NavigateToDiagnosis(val input: DiagnosisInput) : DiagnosePlantEvent()
    object NavigateBack : DiagnosePlantEvent()
}

data class DiagnosePlantUiState(
    val plantId: Long = 0L,
    val moistureLevel: Float = 0.7f,
    val sunLevel: Float = 0.3f,
    val problemDescription: String = "",
    val problemDescriptionError: Boolean = false,
    val imageUri: Uri? = null
)

@HiltViewModel(assistedFactory = DiagnosePlantViewModel.Factory::class)
class DiagnosePlantViewModel @AssistedInject constructor(
    @Assisted private val plantId: Long,
    @Assisted private val initialInput: DiagnosisInput? = null
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(plantId: Long, initialInput: DiagnosisInput? = null): DiagnosePlantViewModel
    }

    private val _state = MutableStateFlow(
        initialInput?.let {
            DiagnosePlantUiState(
                plantId = it.plantId,
                moistureLevel = it.moistureLevel,
                sunLevel = it.sunLevel,
                problemDescription = it.problemDescription,
                imageUri = it.imageUri?.toUri()
            )
        } ?: DiagnosePlantUiState(plantId = plantId)
    )
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

    fun onUriChange(newImageUri: Uri?) {
        _state.update { it.copy(imageUri = newImageUri) }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(DiagnosePlantEvent.NavigateBack)
        }
    }

    private fun validate(): Boolean {
        val descriptionIsBlank = _state.value.problemDescription.isBlank()
        _state.update { it.copy(problemDescriptionError = descriptionIsBlank) }
        return !descriptionIsBlank
    }

    fun onDiagnoseClick() {
        if(!validate()) return
        viewModelScope.launch {
            val currentState = _state.value
            _events.send(DiagnosePlantEvent.NavigateToDiagnosis(
                DiagnosisInput(
                    plantId = currentState.plantId,
                    moistureLevel = currentState.moistureLevel,
                    sunLevel = currentState.sunLevel,
                    problemDescription = currentState.problemDescription,
                    imageUri = currentState.imageUri?.toString()
                )
            ))
        }
    }
}