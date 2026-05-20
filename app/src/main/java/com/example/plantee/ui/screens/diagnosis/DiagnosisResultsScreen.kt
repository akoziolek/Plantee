package com.example.plantee.ui.screens.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.InfoSection
import com.example.plantee.ui.components.base.LabeledSwitch
import com.example.plantee.ui.components.base.PlainImage
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.components.base.ProposedRoutinesListItem
import com.example.plantee.ui.nav.DiagnosisInput
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsEvent
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsUiState
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultsScreen(
    input: DiagnosisInput,
    viewModel: DiagnosisResultsViewModel = hiltViewModel<DiagnosisResultsViewModel, DiagnosisResultsViewModel.Factory> { factory ->
        factory.create(input)
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
            val buttonText = when (state) {
                is DiagnosisResultsUiState.Error -> stringResource(R.string.diagnosis_error_btn_close)
                else -> stringResource(R.string.diagnosis_results_btn_finish)
            }
            
            PrimaryButtonFullWidth(
                text = buttonText,
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
                    PlainImage(
                        imagePath = input.imageUri,
                        name = stringResource(R.string.diagnosis_photo_name)
                    )

                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        InfoSection(
                            headerText = stringResource(R.string.diagnosis_results_label_description),
                            bodyText = currentState.aiDiagnosisResult.diagnosisDescription
                        )
                        InfoSection(
                            headerText = stringResource(R.string.diagnosis_results_label_proposed_routines),
                            bodyText = stringResource(R.string.diagnosis_results_text_proposed_routines)
                        )

                        currentState.aiDiagnosisResult.proposedRoutines.forEach { routine ->
                            ProposedRoutinesListItem(
                                routine = routine,
                                checked = currentState.selectedRoutines.contains(routine.tempId),
                                onCheckedChange = { checked ->
                                    viewModel.onRoutineCheckedChange(routine.tempId, checked)
                                },
                            )
                        }

                        LabeledSwitch(
                            label = stringResource(R.string.diagnosis_results_label_remove_from_routines),
                            checked = currentState.removeFromAssociatedRoutines,
                            onCheckedChange = { viewModel.onRemoveFromRoutinesClick() }
                        )
                    }
                }
            }
            is DiagnosisResultsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Text(
                            text = currentState.message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
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
            input = DiagnosisInput(1L, 0.7f, 0.3f, "Test", null),
            onNavigate = {}
        )
    }
}
