package com.example.plantee.ui.viewmodels.plant

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IMediaRepository
import com.example.plantee.domain.repositories.IPlantsRepository
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
    object NavigateBack : PlantAddEvent()
}

// TODO images
data class PlantAddUiState(
    val name: String = "",
    val nameError: Boolean = false,
    val species: String = "",
    val description: String = "",
    val isFavourite: Boolean = false,
    val imageUri: Uri? = null
)

@HiltViewModel
class PlantAddViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository,
    private val mediaRepository: IMediaRepository
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

    fun onFavouriteChange(newFavourite: Boolean) {
        _state.update { it.copy(isFavourite = newFavourite) }
    }

    fun onUriChange(newUri: Uri?) {
        _state.update { it.copy(imageUri = newUri) }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(PlantAddEvent.NavigateBack)
        }
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
            // TODO - inne zapisywanie, z transakcja
            val media = Media(
                filePath = currentState.imageUri.toString(),
                createdAt = java.time.LocalDateTime.now()
            )
            val mediaId = mediaRepository.createMedia(media)
            val plant = Plant(
                name = currentState.name,
                species = currentState.species,
                description = currentState.description,
                isFavourite = currentState.isFavourite,
                mediaId = mediaId
            )
            val id = plantsRepository.createPlant(plant)
            resetState()
            _events.send(PlantAddEvent.NavigateToDetails(plantId = id))
        }
    }

    fun resetState() {
        _state.value = PlantAddUiState()
    }
}
