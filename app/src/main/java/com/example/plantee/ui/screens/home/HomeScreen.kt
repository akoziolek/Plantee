package com.example.plantee.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
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
import com.example.plantee.ui.components.base.MainTopBar
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.shared.FilterSectionHeader
import com.example.plantee.ui.components.shared.LinkHeader
import com.example.plantee.ui.components.shared.plantListItems_TODELETE
import com.example.plantee.ui.components.shared.todayRoutinesSection
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRoutineClick: (Long) -> Unit,
    onPlantClick: (Long) -> Unit,
    onAddPlantClick: () -> Unit,
    onRoutinesClick: () -> Unit
) {

    Scaffold(
        topBar = {
            MainTopBar(stringResource(R.string.home_title), actions = {
                IconButton(onClick = { /* action 1 */ }) {
                    Icon(Icons.Default.NotificationsNone, "Notifications")
                }
                IconButton(onClick = { /* action 2 */ }) {
                    Icon(Icons.Default.Settings, "Settings")
                }
            })
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.home_btn_add), onClick = onAddPlantClick)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .height(170.dp)
                        .fillMaxWidth()
                ) { }
            }
            item{
                LinkHeader(
                    title = stringResource(R.string.home_label_today_routines),
                    onClick = onRoutinesClick
                )
            }

            todayRoutinesSection(
                routines = List(3) { "Routine $it" },
                onItemClick = onRoutineClick
            )

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
                FilterSectionHeader(
                    title = stringResource(R.string.home_label_your_plants),
                    filterTitle = stringResource(R.string.home_label_filter_plants),
                    onClick = { },
                    sort = SortOrder.NONE
                )
            }

            plantListItems_TODELETE(
                plants = List(10) {"Plant no. $it"},
                onPlantClick = onPlantClick
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PlanteeTheme {
        HomeScreen(
            onRoutineClick = { id -> println("Kliknięto rutynę $id") },
            onPlantClick = { id -> println("Kliknięto planta $id") },
            onAddPlantClick = { println("Kliknięto dodaj planta") },
            onRoutinesClick = { println("Kliknięto lista rutyn") }
        )
    }
}