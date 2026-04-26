package com.example.plantee.ui.screens.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
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
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.DeleteConfirmationDialog
import com.example.plantee.ui.components.base.InfoSection
import com.example.plantee.ui.components.base.OverflowAction
import com.example.plantee.ui.components.base.OverflowMenu
import com.example.plantee.ui.components.base.PlantImage
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.shared.diagnosisListItems
import com.example.plantee.ui.components.shared.routinesSection
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.PlantDetailsEvent
import com.example.plantee.ui.viewmodels.plant.PlantDetailsUiState
import com.example.plantee.ui.viewmodels.plant.PlantDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsScreen(
    plantId: Long,
    viewModel: PlantDetailsViewModel = hiltViewModel<PlantDetailsViewModel, PlantDetailsViewModel.Factory> { factory ->
        factory.create(plantId)
    },
    onNavigate: (PlantDetailsEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val bookMarkIcon =
        if(state is PlantDetailsUiState.Success && (state as PlantDetailsUiState.Success).plant.isFavourite)
            Icons.Default.Bookmark
        else
            Icons.Default.BookmarkBorder

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    if (showDeleteConfirmation) {
        DeleteConfirmationDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            onConfirm = {
                showDeleteConfirmation = false
                viewModel.deletePlant()
            },
            title = stringResource(R.string.delete_plant_dialog_title),
            message = stringResource(R.string.delete_plant_dialog_message),
            confirmText = stringResource(R.string.dialog_confirm),
            dismissText = stringResource(R.string.dialog_cancel)
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.plant_details_title),
                onBackClick = { viewModel.onBackClick() },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavourite() }) {
                        Icon(
                            imageVector = bookMarkIcon,
                            contentDescription = "Toggle favourite"
                        )
                    }
                    OverflowMenu(
                        actions = listOf(
                            OverflowAction(
                                text = stringResource(R.string.menu_edit),
                                onClick = { viewModel.onEditClick() }
                            ),
                            OverflowAction(
                                text = stringResource(R.string.menu_delete),
                                onClick = { showDeleteConfirmation = true }
                            )
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            PrimaryFloatingButton(
                text = stringResource(R.string.plant_details_btn_diagnose),
                onClick = { viewModel.onDiagnoseClick() }
            )
        }
    ) { innerPadding ->
        when (val currentState = state) {
            is PlantDetailsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PlantDetailsUiState.Success -> {
                val plant = currentState.plant
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        PlantImage(
                            name = plant.name,
                            specie = plant.species,
                            state = plant.state,
                            modifier = Modifier
                        )
                    }

                    item {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            InfoSection(
                                headerText = stringResource(R.string.plant_details_label_description),
                                bodyText = plant.description ?: ""
                            )
                            SectionHeader(
                                title = stringResource(R.string.plant_details_label_routines),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }

                    routinesSection(
                        // TODO placeholders
                        routines = plant.routines.map { it.name },
                        onRoutineClick = { id -> viewModel.onRoutineClick(id) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    item {
                        SectionHeader(
                            title = stringResource(R.string.plant_details_label_health_journal),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }

                    diagnosisListItems(
                        // TODO placeholders
                        diagnosis = plant.diagnoses.map { it.diagnosedAt.toString() },
                        onDiagnosisClick = { id -> viewModel.onDiagnosisClick(id) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
            is PlantDetailsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is PlantDetailsUiState.Deleted -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview
@Composable
fun PlantDetailsPreview() {
    PlanteeTheme {
        PlantDetailsScreen(
            plantId = 1L,
            onNavigate = {}
        )
    }
}
