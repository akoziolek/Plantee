package com.example.plantee.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.plantee.ui.components.base.NavItem
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.theme.PlanteeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineAddScreen() {
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)
    val selectedDays = listOf(0, 2, 3, 6)
    var nameText by remember { mutableStateOf("") }
    var descText by remember { mutableStateOf("") }
    val state = rememberSearchBarState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.routine_add_title), onBackClick = { })
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.routine_add_btn_save), onClick = {})
        }
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
                    title = stringResource(R.string.routine_add_label_name),
                    value = nameText,
                    onValueChange = { nameText = it },
                    supportingText = stringResource(R.string.routine_add_support_name)
                )
            }

            // --- DESCRIPTION ---
            item {
                InputTextField(
                    title = stringResource(R.string.routine_add_label_description),
                    value = descText,
                    onValueChange = { descText = it },
                    supportingText = stringResource(R.string.routine_add_support_description)
                )
            }

            // --- DAYS OF THE WEEK ---
            item {
                DaysOfWeek(
                    selectedDays = selectedDays,
                    onDayClick = { }
                )
            }

            // --- CHOOSE PLANTS ---
            item {
                SectionHeader(stringResource(R.string.routine_add_label_plant_choice))
            }

            item {
                SimpleSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    state = state,
                    placeholder = stringResource(R.string.routines_search_bar_placeholder),
                    onExpandedChange = { },
                    expanded = false,
                    onSearch = { }
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            plantListItems(
                plants = List(6) {"Plant no. $it"},
                onPlantClick = {}
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineAddPreview() {
    PlanteeTheme {
        RoutineAddScreen()
    }
}