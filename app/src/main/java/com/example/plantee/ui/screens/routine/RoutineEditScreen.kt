package com.example.plantee.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.RoutineDateFields
import com.example.plantee.ui.components.shared.RoutineFormFields
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.routine.RoutineEditEvent
import com.example.plantee.ui.viewmodels.routine.RoutineEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineEditScreen(
    routineId: Long,
    viewModel: RoutineEditViewModel = hiltViewModel(
        creationCallback = { factory: RoutineEditViewModel.Factory ->
            factory.create(routineId)
        }
    ),
    onNavigate: (RoutineEditEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val text by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.routine_edit_title), onBackClick = { viewModel.onBackClick() })
        },
        floatingActionButton = {
            PrimaryFloatingButton(
                text = stringResource(R.string.routine_edit_btn_save),
                onClick = { viewModel.updateRoutine() },
                modifier = Modifier.imePadding()
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                RoutineFormFields(
                    nameValue = state.name,
                    onNameChange = { viewModel.onNameChange(it) },
                    descriptionValue = state.description,
                    onDescriptionChange = { viewModel.onDescriptionChange(it) }
                )
            }

            item {
                RoutineDateFields(
                    startDateValue = state.startDate,
                    endDateValue = state.endDate,
                    onDateChange = { pair -> viewModel.onDateRangeSelected(pair.first, pair.second) },
                    activeDaysValue = state.activeDays,
                    onActiveDaysChange = { viewModel.onActiveDaysChange(it) }
                )
            }

            item {
                SectionHeader(
                    title = stringResource(R.string.routine_add_label_plant_choice),
                    modifier = Modifier.padding(bottom = 8.dp))
                SimpleSearchBar(
                    query = text,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    state = searchBarState,
                    placeholder = stringResource(R.string.routines_search_bar_placeholder),
                    onExpandedChange = { },
                    expanded = false,
                    onSearch = { }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            plantListItems(
                plants = state.availablePlants,
                onPlantClick = { viewModel.onPlantClick(it) },
                selectedPlantIds = state.selectedPlants
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineEditPreview() {
    PlanteeTheme {
        RoutineEditScreen(
            routineId = 1L,
            onNavigate = {}
        )
    }
}
