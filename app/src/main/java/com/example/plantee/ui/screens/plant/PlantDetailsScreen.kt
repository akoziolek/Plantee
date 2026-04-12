package com.example.plantee.ui.screens.plant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.plantee.ui.components.base.NavBar
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.shared.LinkHeader
import com.example.plantee.ui.components.shared.diagnosisListItems
import com.example.plantee.ui.components.shared.routinesSection
import com.example.plantee.ui.theme.PlanteeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsScreen(
    onDiagnoseClick: () -> Unit,
    onConnectedRoutinesClick: () -> Unit,
    onDiagnosesClick: () -> Unit,
    onRoutineClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onDiagnosisClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.plant_details_title),
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = { /* action 1 */ }) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Add to favourites")
                    }
                    IconButton(onClick = { /* action 2 */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "See more")
                    }
                }
            )
        },
        floatingActionButton = {
            PrimaryFloatingButton(
                text = stringResource(R.string.plant_details_btn_diagnose),
                onClick = onDiagnoseClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                // TODO component with picture and additional labels
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .height(220.dp)
                        .fillMaxWidth()
                ) { }

            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoSection(
                        headerText = stringResource(R.string.plant_details_label_description),
                        bodyText = "I found this plant in the dumpster :( but I managed to bring it back to life"
                    )
                    LinkHeader(
                        title = stringResource(R.string.plant_details_label_routines),
                        onClick = onConnectedRoutinesClick
                    )
                }
            }

            routinesSection(
                routines = List(2) { "Routine $it" },
                onRoutineClick = onRoutineClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            item {
                LinkHeader(
                    title = "Health journal",
                    onClick = onDiagnosesClick,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            diagnosisListItems(
                diagnosis = List(2) {"2$it.01.2026"},
                onDiagnosisClick = onDiagnosisClick,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
@Preview
@Composable
fun PlantDetailsPreview() {
    PlanteeTheme() {
        PlantDetailsScreen(
            onDiagnoseClick = {},
            onConnectedRoutinesClick = {},
            onDiagnosesClick = {},
            onBackClick = {},
            onRoutineClick = {},
            onDiagnosisClick = {}
        )
    }
}
