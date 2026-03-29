package com.example.plantee.ui.screens.routine_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.components.DaysOfWeek
import com.example.plantee.ui.components.InputTextField
import com.example.plantee.ui.components.PlantListItem
import com.example.plantee.ui.components.SectionHeader
import com.example.plantee.ui.components.SimpleSearchBar
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.theme.extendedLight

sealed class NavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : NavItem("Home", Icons.Default.Home, "home")
    object Plants : NavItem("Plants", Icons.Default.LocalFlorist, "plants")
    object Routines : NavItem("Routines", Icons.Default.CalendarMonth, "routines")
}

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
            TopAppBar(
                title = {
                    Text(
                        text = "Edit the routine",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = extendedLight.dimNeutral.colorContainer,
                tonalElevation = 3.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = false,
                        onClick = {  },
                        label = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        icon = {
                            Icon(item.icon, contentDescription = item.title)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.30f),
                            selectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Save routine")
            }
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
                SectionHeader("Name")
                InputTextField(
                    value = nameText,
                    supportingText = "How do you want to name your routine?",
                    onValueChange = { nameText = it }
                )
            }

            // --- DESCRIPTION ---
            item {
                SectionHeader("Description")
                InputTextField(
                    value = descText,
                    supportingText = "Give a short description of the routine",
                    onValueChange = { descText = it }
                )
            }

            // --- DAYS OF THE WEEK ---
            item {
                SectionHeader("Weekdays")
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
                    description =  "Longer description duis aute irure dolor in reprehenderit in voluptate velit of plant no. $index",
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