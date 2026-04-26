package com.example.plantee.ui.screens.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.SimpleSearchBar
import com.example.plantee.ui.components.shared.FilterSectionHeader
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.plant.PlantsEvent
import com.example.plantee.ui.viewmodels.plant.PlantsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantsScreen(
    viewModel: PlantsViewModel = hiltViewModel<PlantsViewModel>(),
    onNavigate: (PlantsEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchBarState = rememberSearchBarState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            onNavigate(event)
        }
    }

    Scaffold(
        topBar = {
            SimpleSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                state = searchBarState,
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
            PrimaryFloatingButton(text = stringResource(R.string.plants_btn_add), onClick = { viewModel.onAddClick() })
        },
    ) { innerPadding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                FilterSectionHeader(
                    title = stringResource(R.string.plants_label_your_plants),
                    filterTitle = stringResource(R.string.home_label_filter_plants),
                    onClick = { //TODO add logic
                    }
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    plantListItems(
                        plants = state.plants,
                        onPlantClick = { viewModel.onPlantClick(it) },
                        onPlantBookmarkClick = { viewModel.onPlantBookmarkClick(it) }
                    )
                }

            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PlantsPreview() {
    PlanteeTheme {
        PlantsScreen(
            onNavigate = {}
        )
    }
}
