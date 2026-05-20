package com.example.plantee.ui.viewmodels.plant

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.Media
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.domain.use_cases.DeleteMediaUseCase
import com.example.plantee.domain.use_cases.SaveMediaUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

sealed class PlantEditEvent {
    object NavigateBack : PlantEditEvent()
    object PlantUpdated : PlantEditEvent()
}

data class PlantEditUiState(
    val id: Long = 0L,
    val name: String = "",
    val nameError: Boolean = false,
    val species: String = "",
    val description: String = "",
    val isFavourite: Boolean = false,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val imageUri: Uri? = null,
    val media: Media? = null
)

@HiltViewModel(assistedFactory = PlantEditViewModel.Factory::class)
class PlantEditViewModel @AssistedInject constructor(
    private val plantsRepository: IPlantsRepository,
    private val saveMediaUseCase: SaveMediaUseCase,
    private val deleteMediaUseCase: DeleteMediaUseCase,
    @Assisted private val plantId: Long
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(plantId: Long): PlantEditViewModel
    }

    private val _state = MutableStateFlow(PlantEditUiState())
    val state: StateFlow<PlantEditUiState> = _state.asStateFlow()

    private val _events = Channel<PlantEditEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadPlant()
    }

    private fun loadPlant() {
        if(_state.value.isSaving) return
        viewModelScope.launch {
            val plant = plantsRepository.getPlant(plantId).first()
            plant?.let { p ->
                _state.update {
                    it.copy(
                        id = p.id,
                        name = p.name,
                        species = p.species ?: "",
                        description = p.description ?: "",
                        isFavourite = p.isFavourite,
                        imageUri = if (p.media != null) Uri.fromFile(File(p.media.filePath)) else null,
                        isLoading = false,
                        media = p.media
                    )
                }
            }
        }
    }

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
            _events.send(PlantEditEvent.NavigateBack)
        }
    }

    private fun validate(): Boolean {
        val nameIsBlank = _state.value.name.isBlank()
        _state.update { it.copy(nameError = nameIsBlank) }
        return !nameIsBlank
    }

    fun updatePlant() {
        if (!validate()) return

        viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(isSaving = true) }

            val oldMediaUri = currentState.media?.let { Uri.fromFile(File(it.filePath)) }
            val imageChanged = currentState.imageUri != oldMediaUri

            var newMedia = currentState.media

            if (imageChanged) {
                newMedia = if (currentState.imageUri != null) {
                    saveMediaUseCase(newImageUri = currentState.imageUri)
                } else {
                    null
                }
            }

            val plant = Plant(
                id = currentState.id,
                name = currentState.name,
                species = currentState.species,
                description = currentState.description,
                isFavourite = currentState.isFavourite,
                media = newMedia
            )

            plantsRepository.updatePlant(plant)
            if (imageChanged && currentState.media != null) {
                deleteMediaUseCase(currentState.media)
            }

            _events.send(PlantEditEvent.PlantUpdated)
        }
    }
}
