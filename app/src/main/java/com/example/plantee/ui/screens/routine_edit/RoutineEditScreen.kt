package com.example.plantee.ui.screens.routine_edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.DaysOfWeek
import com.example.plantee.ui.components.base.InputTextField
import com.example.plantee.ui.components.base.NavBar
import com.example.plantee.ui.components.base.NavItem
import com.example.plantee.ui.components.base.PlantListItem
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.theme.PlanteeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineEditScreen() {
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)
    val selectedDays = listOf(0, 2, 3, 6)
    var nameText by remember { mutableStateOf("Routine name") }
    var descText by remember { mutableStateOf("Routine description") }
    val state = rememberSearchBarState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.routines_add_title), onBackClick = { })
        },
        bottomBar = {
            NavBar(items)
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.routine_btn_save), onClick = {})
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // --- NAME ---
            item {
                InputTextField(
                    title = "Name",
                    value = nameText,
                    onValueChange = { nameText = it },
                    supportingText = "How do you want to name your routine?"
                )
            }

            // --- DESCRIPTION ---
            item {
                InputTextField(
                    title = "Description",
                    value = descText,
                    onValueChange = { descText = it },
                    supportingText = "Give a short description of the routine"
                )
            }

            // --- DAYS OF THE WEEK ---
            item {
                SectionHeader(title = "Weekdays")
                DaysOfWeek(
                    selectedDays = selectedDays,
                    onDayClick = { }
                )
            }

            // --- CHOOSE PLANTS ---
            item {
                SectionHeader("Choose plants")
            }

            item {
                SimpleSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    state = state,
                    placeholder = "Search for a plant"
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(5) { index ->
                PlantListItem(
                    title = "Plant no. $index",
                    description = "Longer description duis aute irure dolor in reprehenderit in voluptate velit of plant no. $index",
                    onClick = { }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineEditPreview() {
    PlanteeTheme {
        RoutineEditScreen()
    }
}