package com.example.plantee.ui.screens.plant

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
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.PlantEditEvent
import com.example.plantee.ui.viewmodels.plant.PlantEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantEditScreen(
    plantId: Long,
    viewModel: PlantEditViewModel = hiltViewModel<PlantEditViewModel, PlantEditViewModel.Factory> { factory ->
        factory.create(plantId)
    },
    onNavigate: (PlantEditEvent) -> Unit
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
                title = stringResource(R.string.plant_edit_title),
                onBackClick = { viewModel.onBackClick() })
        },
        bottomBar = {
            PrimaryButtonFullWidth(
                text = stringResource(R.string.plant_edit_btn_save),
                onClick = { viewModel.updatePlant() },
                modifier = Modifier.padding(10.dp)
            )
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // TODO real images
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .height(220.dp)
                        .fillMaxWidth()
                ) { }

                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PlantFormFields(
                        nameValue = state.name,
                        onNameChange = { viewModel.onNameChange(it) },
                        speciesValue = state.species,
                        onSpeciesChange = { viewModel.onSpeciesChange(it) },
                        descriptionValue = state.description,
                        onDescriptionChange = { viewModel.onDescriptionChange(it) },
                        nameError = state.nameError
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EditPlantEditPreview() {
    PlanteeTheme {
        PlantEditScreen(
            plantId = 1L,
            onNavigate = {}
        )
    }
}
