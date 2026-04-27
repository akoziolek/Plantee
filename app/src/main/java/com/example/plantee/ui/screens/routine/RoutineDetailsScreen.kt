package com.example.plantee.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.DaysOfWeek
import com.example.plantee.ui.components.base.InfoSection
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.shared.plantListItems_TODELETE
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.routine.RoutineDetailsEvent
import com.example.plantee.ui.viewmodels.routine.RoutineDetailsUiState
import com.example.plantee.ui.viewmodels.routine.RoutineDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailsScreen(
    routineId: Long,
    viewModel: RoutineDetailsViewModel = hiltViewModel(
        creationCallback = { factory: RoutineDetailsViewModel.Factory ->
            factory.create(routineId)
        }
    ),
    onNavigate: (RoutineDetailsEvent) -> Unit
//    onPlantClick: (Long) -> Unit,
//    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
//    val selectedDays = 64

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                stringResource(R.string.routine_details_title),
                onBackClick = { viewModel.onBackClick() },
                actions = {
                    IconButton(onClick = { /* action 1 */ }) {
                        Icon(Icons.Default.BookmarkBorder, "Add to favourites")
                    }
                    IconButton(onClick = { /* action 2 */ }) {
                        Icon(Icons.Default.MoreVert, "See more")
                    }
                })
        },
    ) { innerPadding ->
        when (val currentState = state) {
            is RoutineDetailsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is RoutineDetailsUiState.Success -> {
                val routine = currentState.routine
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // --- TITLE ---
                    item {
                        Text(
                            text = routine.name,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    // --- DESCRIPTION ---
                    item {
                        InfoSection(
                            headerText = stringResource(R.string.routine_details_label_description),
                            bodyText = routine.description ?: ""
                        )
                    }

                    // --- DAYS OF THE WEEK ---
                    item {
                        DaysOfWeek(
                            selectedDays = routine.activeDays ?: 0,
                            onDayClick = { },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // --- PLANTS ---
                    item {
                        SectionHeader(
                            title = stringResource(R.string.routine_details_label_plants),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    plantListItems_TODELETE(
                        plants = List(6) { "Plant no. $it" },
                        onPlantClick = { }
                    )
                }
            }
            is RoutineDetailsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is RoutineDetailsUiState.Deleted -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineDetailsPreview() {
    PlanteeTheme {
        RoutineDetailsScreen(
            routineId = 1L,
            onNavigate = {}
//            onBackClick = {},
//            onPlantClick = {}
        )
    }
}