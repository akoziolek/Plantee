package com.example.plantee.ui.screens.routine

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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.FilterBar
import com.example.plantee.ui.components.base.NavBar
import com.example.plantee.ui.components.base.NavItem
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.routinesSection
import com.example.plantee.ui.components.shared.todayRoutinesSection
import com.example.plantee.ui.theme.PlanteeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen() {
    val state = rememberSearchBarState()
    var query by remember { mutableStateOf("") }
    var selectedItem by remember { mutableIntStateOf(2) }
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)

    Scaffold(
        topBar = {
            SimpleSearchBar(
                query = query,
                onQueryChange = { query = it },
                state = state,
                placeholder = stringResource(R.string.routines_search_bar_placeholder),
                expanded = false,
                onExpandedChange = { },
                onSearch = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
                    .padding(horizontal = 16.dp),
            )
        },
        bottomBar = {
            NavBar(items)
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.routines_btn_add), onClick = {})
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                SectionHeader(stringResource(R.string.routines_label_for_today))
            }
            // ewentualnie bardziej odsunac od gory
            todayRoutinesSection(
                routines = List(2) { "Routine $it" },
                onItemClick = { /* ... */ }
            )

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                SectionHeader(stringResource(R.string.routines_label_all))
                FilterBar({}, {})
            }

            routinesSection(
                routines = List(6) { "Routine $it" },
                onRoutineClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutinesPreview() {
    PlanteeTheme {
        RoutinesScreen()
    }
}