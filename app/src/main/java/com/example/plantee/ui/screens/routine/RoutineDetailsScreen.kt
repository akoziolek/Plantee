package com.example.plantee.ui.screens.routine

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.components.base.BackTopBar
import com.example.plantee.ui.components.base.DaysOfWeek
import com.example.plantee.ui.components.base.InfoSection
import com.example.plantee.ui.components.base.NavBar
import com.example.plantee.ui.components.base.SectionHeader
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.theme.PlanteeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailsScreen(
    onPlantClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val selectedDays = listOf(0, 2, 3, 6)

    Scaffold(
        topBar = {
            BackTopBar(stringResource(R.string.routine_details_title), onBackClick = onBackClick, actions = {
                IconButton(onClick = { /* action 1 */ }) {
                    Icon(Icons.Default.BookmarkBorder, "Add to favourites")
                }
                IconButton(onClick = { /* action 2 */ }) {
                    Icon(Icons.Default.MoreVert, "See more")
                }
            })
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // --- TITLE ---
            item {
                Text(
                    text = stringResource(R.string.routine_details_label_name),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // --- DESCRIPTION ---
            item {
                InfoSection(
                    headerText = stringResource(R.string.routine_details_label_description),
                    bodyText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sollicitudin libero nec velit commodo, ac imperdiet diam egestas."
                )
            }

            // --- DAYS OF THE WEEK ---
            item {
                DaysOfWeek(
                    selectedDays = selectedDays,
                    onDayClick = { },
                    modifier = Modifier.padding(top= 8.dp)
                )
            }

            // --- PLANTS ---
            item {
                SectionHeader(
                    title = stringResource(R.string.routine_details_label_plants),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            plantListItems(
                plants = List(6) {"Plant no. $it"},
                onPlantClick = onPlantClick
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun RoutineDetailsPreview() {
    PlanteeTheme {
        RoutineDetailsScreen(
            onBackClick = {},
            onPlantClick = {}
        )
    }
}