package com.example.plantee.ui.screens.plant_forms

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.LabeledSwitch
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPlantScreen() {
    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.plant_add_title),
                onBackClick = {})
        },
        bottomBar = {
            PrimaryButtonFullWidth(
                text = stringResource(R.string.plant_add_btn_add),
                onClick = {},
                modifier = Modifier.padding(10.dp)
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = innerPadding.calculateTopPadding())
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
                    nameValue = "Plant1",
                    onNameChange = {},
                    speciesValue = "Philodendron",
                    onSpeciesChange = {},
                    descriptionValue = "Nice",
                    onDescriptionChange = {}
                )

                LabeledSwitch(
                    label = "Create first entry",
                    checked = true,
                    onCheckedChange = {},
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun AddPlantPreview() {
    PlanteeTheme() {
        AddPlantScreen()
    }
}
