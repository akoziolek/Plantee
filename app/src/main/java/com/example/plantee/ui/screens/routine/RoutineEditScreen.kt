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
import com.example.plantee.ui.components.shared.plantListItems_TODELETE
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.routine.RoutineAddEvent
import com.example.plantee.ui.viewmodels.routine.RoutineAddViewModel
import com.example.plantee.ui.viewmodels.routine.RoutineEditEvent
import com.example.plantee.ui.viewmodels.routine.RoutineEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineEditScreen(
    viewModel: RoutineEditViewModel = hiltViewModel<RoutineEditViewModel>(),
    onNavigate: (RoutineEditEvent) -> Unit
//    onSaveRoutineClick: () -> Unit,
//    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val text by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchBarState = rememberSearchBarState()

//    val selectedDays = 64
//    var nameText by remember { mutableStateOf("Routine name") }
//    var descText by remember { mutableStateOf("Routine description") }
//    val state = rememberSearchBarState()
//    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.routine_edit_title), onBackClick = { viewModel.onBackClick() })
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.routine_edit_btn_save), onClick = { viewModel.updateRoutine() })
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
                    title = stringResource(R.string.routine_edit_label_name),
                    value = state.name,
                    onValueChange = { viewModel.onNameChange(it) },
                    supportingText = stringResource(R.string.routine_edit_support_name)
                )
            }

            // --- DESCRIPTION ---
            item {
                InputTextField(
                    title = stringResource(R.string.routine_edit_label_description),
                    value = state.description,
                    onValueChange = { viewModel.onDescriptionChange(it) },
                    supportingText = stringResource(R.string.routine_edit_support_description)
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
                SectionHeader(stringResource(R.string.routine_edit_label_plant_choice))
            }

            item {
                SimpleSearchBar(
                    query = text,
                    onQueryChange = { viewModel.onSearchQueryChange(it) },
                    state = searchBarState,
                    placeholder = stringResource(R.string.plants_search_bar_placeholder),
                    expanded = false,
                    onExpandedChange = { },
                    onSearch = { }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            plantListItems_TODELETE(
                plants = List(6) {"Plant no. $it"},
                onPlantClick = {}
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineEditPreview() {
    PlanteeTheme {
        RoutineEditScreen(
            onNavigate = {},
//            onSaveRoutineClick = {},
//            onBackClick = {}
        )
    }
}