package com.example.plantee.ui.screens.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import com.example.plantee.ui.components.base.NavBar
import com.example.plantee.ui.components.base.NavItem
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.FilterSectionHeader
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.theme.PlanteeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantsScreen() {
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)
    val state = rememberSearchBarState()
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            SimpleSearchBar(
                query = query,
                onQueryChange = { query = it },
                state = state,
                placeholder = stringResource(R.string.plants_search_bar_placeholder),
                expanded = false,
                onExpandedChange = { },
                onSearch = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
                    .padding(horizontal = 16.dp),
            )
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.plants_btn_add), onClick = {})
        },
        bottomBar = {
            NavBar(items)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterSectionHeader(
                    title = stringResource(R.string.plants_label_your_plants),
                    filterTitle = stringResource(R.string.plants_label_filter_plants)
                ) { }
            }

            plantListItems(
                plants = List(10) {"Plant no. $it"},
                onPlantClick = {}
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PlantsPreview() {
    PlanteeTheme {
        PlantsScreen()
    }
}