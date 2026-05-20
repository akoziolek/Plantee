package com.example.plantee.ui.screens.diagnosis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.plantee.ui.components.base.PlainImage
import com.example.plantee.ui.components.base.RoutinesListItem
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisDetailsEvent
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisDetailsUiState
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisDetailsScreen(
    diagnosisId: Long,
    viewModel: DiagnosisDetailsViewModel = hiltViewModel<DiagnosisDetailsViewModel, DiagnosisDetailsViewModel.Factory> { factory ->
        factory.create(diagnosisId)
    },
    onNavigate: (DiagnosisDetailsEvent) -> Unit
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
                title = stringResource(R.string.diagnosis_details_title),
                onBackClick = { viewModel.onBackClick() })
        }
    ) { innerPadding ->
        when (val currentState = state) {
            is DiagnosisDetailsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is DiagnosisDetailsUiState.Success -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    PlainImage(
                        imagePath = currentState.diagnosis.media?.filePath,
                        name = stringResource(R.string.diagnosis_photo_name)
                    )

                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        InfoSection(
                            headerText = stringResource(R.string.diagnosis_details_label_description),
                            bodyText = currentState.diagnosis.response
                        )
                        if(currentState.diagnosis.routines.isNotEmpty()) {
                            InfoSection(
                                headerText = stringResource(R.string.diagnosis_details_label_routines),
                                bodyText = stringResource(R.string.diagnosis_details_description_routines)
                            )

                            currentState.diagnosis.routines.forEach { routine ->
                                RoutinesListItem(
                                    headlineText = routine.name,
                                    supportingText = routine.description ?: "",
                                    onClick = { viewModel.onRoutineClick(routine.id) }
                                )
                            }
                        }
                    }
                }
            }
            is DiagnosisDetailsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview
@Composable
fun DiagnosisDetailsPreview() {
    PlanteeTheme {
        DiagnosisDetailsScreen(
            diagnosisId = 1L,
            onNavigate = {}
        )
    }
}
