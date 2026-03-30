package com.example.plantee.ui.screens.plant_forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.shared.PlantFormFields
import com.example.plantee.ui.theme.PlanteeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantFormScreen() {
    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.plant_add_title),
                onBackClick = {})
        },
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            PlantFormFields(
                "Plant1",
                {},
                "Philodendron",
                {},
                "Nice",
                {}
            )

        }
    }
}

@Preview
@Composable
fun PlantFormPreview() {
    PlanteeTheme() {
        PlantFormScreen()
    }
}
