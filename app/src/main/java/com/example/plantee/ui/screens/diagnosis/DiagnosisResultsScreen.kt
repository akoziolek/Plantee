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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.InfoSection
import com.example.plantee.ui.components.base.LabeledSwitch
import com.example.plantee.ui.components.base.PrimaryButtonFullWidth
import com.example.plantee.ui.components.base.RoutinesListItem
import com.example.plantee.ui.theme.PlanteeTheme

// TODO create a diagnosis details screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisDetailsScreen() {
    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.diagnosis_results_title),
                onBackClick = {})
        },
        bottomBar = {
            PrimaryButtonFullWidth(
                text = stringResource(R.string.diagnosis_results_btn_finish),
                onClick = {},
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
                InfoSection(
                    headerText = stringResource(R.string.diagnosis_results_label_description),
                    bodyText = "This often indicates low humidity or underwatering. We recommend increasing misting and checking the soil moisture."
                )
                InfoSection(
                    headerText = stringResource(R.string.diagnosis_results_label_proposed_routines),
                    bodyText = stringResource(R.string.diagnosis_results_text_proposed_routines)
                )

                // TODO paste the routines list component
                RoutinesListItem("Weekend watering", "Use small amount of water", onCheckedChange = {})

                // FIXME weird two lined label?
                LabeledSwitch(
                    label = stringResource(R.string.diagnosis_results_label_remove_from_routines),
                    checked = true,
                    onCheckedChange = {}
                )
            }
        }
    }
}

@Preview
@Composable
fun DiagnosisDetailsPreview() {
    PlanteeTheme() {
        DiagnosisDetailsScreen()
    }
}
