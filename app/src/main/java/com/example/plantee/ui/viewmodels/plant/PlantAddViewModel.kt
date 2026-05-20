package com.example.plantee.ui.viewmodels.plant

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.use_cases.SaveMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlantAddEvent {
    data class NavigateToDetails(val plantId: Long) : PlantAddEvent()
    data class NavigateToDiagnose(val plantId: Long) : PlantAddEvent()
    object NavigateBack : PlantAddEvent()
}

data class PlantAddUiState(
    val name: String = "",
    val nameError: Boolean = false,
    val species: String = "",
    val description: String = "",
    val isFavourite: Boolean = false,
    val imageUri: Uri? = null,
    val createFirstEntry: Boolean = false
)

@HiltViewModel
class PlantAddViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val saveMediaUseCase: SaveMediaUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PlantAddUiState())
    val state: StateFlow<PlantAddUiState> = _state.asStateFlow()

    private val _events = Channel<PlantAddEvent>()
    val events = _events.receiveAsFlow()

    fun onNameChange(newName: String) {
        _state.update { it.copy(name = newName, nameError = false) }
    }

    fun onSpeciesChange(newSpecies: String) {
        _state.update { it.copy(species = newSpecies) }
    }

    fun onDescriptionChange(newDescription: String) {
        _state.update { it.copy(description = newDescription) }
    }

    fun onUriChange(newUri: Uri?) {
        _state.update { it.copy(imageUri = newUri) }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(PlantAddEvent.NavigateBack)
        }
    }

    fun onCreateFirstEntryChange(newCreateFirstEntry: Boolean) {
        _state.update { it.copy(createFirstEntry = newCreateFirstEntry) }
    }

    private fun validate(): Boolean {
        val nameIsBlank = _state.value.name.isBlank()
        _state.update { it.copy(nameError = nameIsBlank) }
        return !nameIsBlank
    }

    fun savePlant() {
        if (!validate()) return

        viewModelScope.launch {
            val currentState = _state.value
            val media = currentState.imageUri?.let {
                saveMediaUseCase(it)
            }

            val plant = Plant(
                name = currentState.name,
                species = currentState.species,
                description = currentState.description,
                isFavourite = currentState.isFavourite,
                media = media
            )
            val id = plantsRepository.createPlant(plant)
            val event = if (currentState.createFirstEntry) PlantAddEvent.NavigateToDiagnose(plantId = id)
                else PlantAddEvent.NavigateToDetails(plantId = id)
            _events.send(event)
        }
    }
}
