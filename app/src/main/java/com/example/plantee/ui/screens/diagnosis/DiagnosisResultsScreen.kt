package com.example.plantee.ui.screens.diagnosis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.plantee.ui.components.base.InfoSection
import com.example.plantee.ui.components.base.LabeledSwitch
import com.example.plantee.ui.components.base.PlainPlantImage
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.components.base.RoutinesListItem
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsEvent
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsUiState
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultsScreen(
    diagnosisId: Long,
    viewModel: DiagnosisResultsViewModel = hiltViewModel<DiagnosisResultsViewModel, DiagnosisResultsViewModel.Factory> { factory ->
        factory.create(diagnosisId)
    },
    onNavigate: (DiagnosisResultsEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.diagnosis_results_title),
                onBackClick = { viewModel.onBackClick() })
        },
        bottomBar = {
            PrimaryButtonFullWidth(
                text = stringResource(R.string.diagnosis_results_btn_finish),
                onClick = { viewModel.onFinishClick() },
                modifier = Modifier.padding(10.dp)
            )
        }
    ) { innerPadding ->
        when (val currentState = state) {
            is DiagnosisResultsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DiagnosisResultsUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // TODO change to currentState.diagnosis.media.filePath
                    val imagePath = if (currentState.diagnosis.listOfMedia.isNotEmpty()) currentState.diagnosis.listOfMedia[0].filePath else null
                    PlainPlantImage(
                        imagePath = imagePath,
                        name = null // TODO change to sth
                    )

                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        InfoSection(
                            headerText = stringResource(R.string.diagnosis_results_label_description),
                            bodyText = currentState.diagnosis.response ?: "THERE WILL BE SOME AI RESPONSE"
                        )
                        InfoSection(
                            headerText = stringResource(R.string.diagnosis_results_label_proposed_routines),
                            bodyText = stringResource(R.string.diagnosis_results_text_proposed_routines)
                        )

                        currentState.proposedRoutines.forEach { routine ->
                            RoutinesListItem(
                                headlineText = routine.name,
                                supportingText = routine.description ?: "",
                                checked = currentState.selectedRoutines.contains(routine.id),
                                onCheckedChange = { checked ->
                                    viewModel.onRoutineCheckedChange(routine.id, checked)
                                },
                                onClick = { viewModel.onRoutineClick(routine.id) }
                            )
                        }

                        LabeledSwitch(
                            label = stringResource(R.string.diagnosis_results_label_remove_from_routines),
                            checked = false, // TODO logic for removal
                            onCheckedChange = {}
                        )
                    }
                }
            }
            is DiagnosisResultsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview
@Composable
fun DiagnosisResultsPreview() {
    PlanteeTheme {
        DiagnosisResultsScreen(
            diagnosisId = 1L,
            onNavigate = {}
        )
    }
}
