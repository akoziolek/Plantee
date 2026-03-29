package com.example.plantee.ui.screens.plant_forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.components.BackTopBar
import com.example.plantee.ui.theme.PlanteeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantFormScreen() {
    Scaffold(
        topBar = {
            BackTopBar(
                title = "Add a routine",
                onBackClick = {})
        },
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text =
                    $$"""
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.

                    You have pressed the floating action button $presses times.
                """.trimIndent(),
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
