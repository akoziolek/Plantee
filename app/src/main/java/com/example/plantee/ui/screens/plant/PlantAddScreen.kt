package com.example.plantee.ui.screens.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.LabeledSwitch
import com.example.plantee.ui.components.base.PhotoPicker
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.PlantAddEvent
import com.example.plantee.ui.viewmodels.plant.PlantAddViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantAddScreen(
    viewModel: PlantAddViewModel = hiltViewModel<PlantAddViewModel>(),
    onNavigate: (PlantAddEvent) -> Unit
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
                title = stringResource(R.string.plant_add_title),
                onBackClick = { viewModel.onBackClick() }
            )
        },
        bottomBar = {
            PrimaryButtonFullWidth(
                text = stringResource(R.string.plant_add_btn_add),
                onClick = { viewModel.savePlant() },
                modifier = Modifier.padding(10.dp).imePadding()
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            PhotoPicker (
                selectedUri = state.imageUri,
                onPhotoSelected = { viewModel.onUriChange(it)}
            )

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

                LabeledSwitch(
                    label = stringResource(R.string.plant_add_switch_label),
                    checked = state.createFirstEntry,
                    onCheckedChange = { viewModel.onCreateFirstEntryChange(it) },
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PlantAddPreview() {
    PlanteeTheme {
        PlantAddScreen(
            onNavigate = {}
        )
    }
}
