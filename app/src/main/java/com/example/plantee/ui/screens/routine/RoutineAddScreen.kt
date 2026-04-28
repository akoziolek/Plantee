package com.example.plantee.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.DaysOfWeek
import com.example.plantee.ui.components.base.InputTextField
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.components.shared.plantListItems_TODELETE
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.PlantAddEvent
import com.example.plantee.ui.viewmodels.plant.PlantAddViewModel
import com.example.plantee.ui.viewmodels.routine.RoutineAddEvent
import com.example.plantee.ui.viewmodels.routine.RoutineAddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineAddScreen(
    viewModel: RoutineAddViewModel = hiltViewModel<RoutineAddViewModel>(),
    onNavigate: (RoutineAddEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val text by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sort by viewModel.sortOrder.collectAsStateWithLifecycle()
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.routine_add_title), onBackClick = { viewModel.onBackClick() })
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.routine_add_btn_save), onClick = { viewModel.saveRoutine() })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // --- NAME ---
            item {
                InputTextField(
                    title = stringResource(R.string.routine_add_label_name),
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    supportingText = stringResource(R.string.routine_add_support_name)
                )
            }

            // --- DESCRIPTION ---
            item {
                InputTextField(
                    title = stringResource(R.string.routine_add_label_description),
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    supportingText = stringResource(R.string.routine_add_support_description)
                )
            }

            // --- DAYS OF THE WEEK ---
            item {
                DaysOfWeek(
                    selectedDays = state.activeDays,
                    onDayClick = { viewModel.onActiveDaysChange(it) }
                )
            }

            // --- CHOOSE PLANTS ---
            item {
                SectionHeader(stringResource(R.string.routine_add_label_plant_choice))
            }

            item {
                SimpleSearchBar(
                    query = text,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    state = searchBarState,
                    placeholder = stringResource(R.string.routines_search_bar_placeholder),
                    onExpandedChange = { },
                    expanded = false,
                    onSearch = { }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            plantListItems(
                plants = state.plants,
                onPlantClick = {  }, // TODO select plant
                onPlantBookmarkClick = {  }
            )

        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineAddPreview() {
    PlanteeTheme {
        RoutineAddScreen(
            onNavigate = { }
        )
    }
}