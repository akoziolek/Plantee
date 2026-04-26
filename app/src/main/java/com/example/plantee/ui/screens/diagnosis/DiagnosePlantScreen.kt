package com.example.plantee.ui.screens.diagnosis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.plantee.ui.components.base.InputSlider
import com.example.plantee.ui.components.base.InputTextField
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosePlantEvent
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosePlantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosePlantScreen(
    plantId: Long,
    viewModel: DiagnosePlantViewModel = hiltViewModel<DiagnosePlantViewModel, DiagnosePlantViewModel.Factory> { factory ->
        factory.create(plantId)
    },
    onNavigate: (DiagnosePlantEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.diagnosis_form_title),
                onBackClick = { viewModel.onBackClick() })
        },
        bottomBar = {
            PrimaryButtonFullWidth(
                text = stringResource(R.string.plant_diagnosis_btn_diagnose),
                onClick =  { viewModel.onDiagnoseClick() },
                modifier = Modifier.padding(10.dp)
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // TODO real images - remember about the extra button on top
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .height(220.dp)
                    .fillMaxWidth()
            ) { }

            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.plant_form_text_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                InputSlider(
                    title = stringResource(R.string.plant_form_label_sun),
                    value = state.sunLevel,
                    onValueChange = { viewModel.onSunLevelChange(it) }
                )
                InputSlider(
                    title = stringResource(R.string.plant_form_label_moisture),
                    value = state.moistureLevel,
                    onValueChange = { viewModel.onMoistureLevelChange(it) }
                )
                InputTextField(
                    title = stringResource(R.string.plant_form_label_description),
                    value = state.problemDescription,
                    onValueChange = { viewModel.onProblemDescriptionChange(it) },
                    modifier = Modifier.padding(top = 8.dp),
                    supportingText = stringResource(R.string.plant_form_support_description),
                    singleLine = false,
                    minLines = 3,
                    isError = state.problemDescriptionError,
                    errorText = "Description is required"
                )
            }
        }
    }
}

@Preview
@Composable
fun DiagnosePlantPreview() {
    PlanteeTheme() {
        DiagnosePlantScreen(
            onNavigate = {},
            plantId = 0
        )
    }
}
