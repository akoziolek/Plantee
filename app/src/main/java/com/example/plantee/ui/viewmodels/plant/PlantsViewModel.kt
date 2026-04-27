package com.example.plantee.ui.viewmodels.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.repositories.IPlantsRepository
import com.example.plantee.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PlantsEvent {
    data class NavigateToDetails(val plantId: Long) : PlantsEvent()
    object NavigateToAdd : PlantsEvent()
    object NavigateBack : PlantsEvent()
}

data class PlantsUiState(
    val plants: List<PlantSummary> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

@HiltViewModel
class PlantsViewModel @Inject constructor(
    private val plantsRepository: IPlantsRepository
) : ViewModel() {
    private val _events = Channel<PlantsEvent>()
    val events = _events.receiveAsFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)
    val sortOrder = _sortOrder.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val state: StateFlow<PlantsUiState> = combine(
        _searchQuery.debounce(300L).distinctUntilChanged(),
        _sortOrder
    ) { query, sort -> query to sort }
        .flatMapLatest { (query, sort) ->
            plantsRepository.getSearchedPlantsSummaryWithSort(query, sort).map { filtered ->
                PlantsUiState(
                    plants = filtered,
                    isLoading = false
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PlantsUiState()
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun toggleSortOrder() {
        _sortOrder.value = _sortOrder.value.next()
    }

    fun onPlantClick(plantId: Long) {
        viewModelScope.launch {
            _events.send(PlantsEvent.NavigateToDetails(plantId))
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            _events.send(PlantsEvent.NavigateBack)
        }
    }

    fun onAddClick() {
        viewModelScope.launch {
            _events.send(PlantsEvent.NavigateToAdd)
        }
    }

    fun onPlantBookmarkClick(plantId: Long) {
        viewModelScope.launch {
            val currentState = state.value
            plantsRepository.togglePlantFavourite(plantId)
        }
    }
}
