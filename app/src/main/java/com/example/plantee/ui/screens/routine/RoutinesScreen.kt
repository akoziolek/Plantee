package com.example.plantee.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.FilterBar
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.components.shared.routinesSection
import com.example.plantee.ui.components.shared.routinesSection_TODELETE
import com.example.plantee.ui.components.shared.todayRoutinesSection
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.PlantsEvent
import com.example.plantee.ui.viewmodels.plant.PlantsViewModel
import com.example.plantee.ui.viewmodels.routine.RoutinesEvent
import com.example.plantee.ui.viewmodels.routine.RoutinesViewModel
import com.example.plantee.utils.SortOrder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(
    viewModel: RoutinesViewModel = hiltViewModel<RoutinesViewModel>(),
    onNavigate: (RoutinesEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val text by viewModel.searchQuery.collectAsStateWithLifecycle()
    val sort by viewModel.sortOrder.collectAsStateWithLifecycle()
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            SimpleSearchBar(
                query = text,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                state = searchBarState,
                placeholder = stringResource(R.string.routines_search_bar_placeholder),
                expanded = false,
                onExpandedChange = { },
                onSearch = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
                    .padding(horizontal = 16.dp),
            )
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.routines_btn_add), onClick = { viewModel.onAddClick() })
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    SectionHeader(stringResource(R.string.routines_label_for_today))
                }
                // TODO ewentualnie bardziej odsunac od gory
                todayRoutinesSection(
                    routines = state.todayRoutines,
                    onCheckboxClick = { viewModel.onCheckboxClick(it)},
                    onItemClick = { viewModel.onRoutineClick(it) }
                )

                item { Spacer(modifier = Modifier.height(24.dp)) }

                item {
                    SectionHeader(stringResource(R.string.routines_label_all))
                    FilterBar({}, {}, sort = SortOrder.NONE)
                }

                routinesSection(
                    routines = state.routines,
                    onRoutineClick = { viewModel.onRoutineClick(it) }
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutinesPreview() {
    PlanteeTheme {
        RoutinesScreen(
            onNavigate = {}
//            onRoutineAddClick = { println("Kliknięto dodaj rutynę") },
//            onRoutineClick = { id -> println("Kliknięto rutynę $id") }
        )
    }
}