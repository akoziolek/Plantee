package com.example.plantee.ui.screens.routines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.theme.extendedLight
import com.example.plantee.ui.theme.surfaceVariantLight

sealed class NavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : NavItem("Home", Icons.Default.Home, "home")
    object Plants : NavItem("Plants", Icons.Default.LocalFlorist, "plants")
    object Routines : NavItem("Routines", Icons.Default.CalendarMonth, "routines")
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen() {
    val state = rememberSearchBarState()
    var query by remember { mutableStateOf("") }
    var selectedItem by remember { mutableIntStateOf(2) }
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)

    Scaffold(
        topBar = {
            SearchBar(
                state = state,
                inputField = {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search for a routine") },
                        trailingIcon     = {Icon(Icons.Default.Search, contentDescription = null)},
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .safeDrawingPadding(),
                colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                shape = SearchBarDefaults.inputFieldShape,
                tonalElevation = SearchBarDefaults.TonalElevation,
                shadowElevation = SearchBarDefaults.ShadowElevation,
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = extendedLight.dimNeutral.colorContainer,
                tonalElevation = 3.dp
            ) {
                items.forEachIndexed { index, item ->
                  NavigationBarItem(
                      selected = selectedItem == index,
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
                Text(text = "Add routine")
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
            // --- FOR TODAY ---
            item {
                SectionHeader("For today")
            }
            items(2) { index ->
                ListItem(
                    headlineContent = { Text("Routine no. $index") },
                    supportingContent = { Text("Description for routine no. $index") },
                    leadingContent = {
                        Checkbox(checked = false, onCheckedChange = { })
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = extendedLight.darkNeutral.colorContainer.copy(alpha = 0.45f)
                    )
                )
            }

            // --- SPACING ---
            item { Spacer(modifier = Modifier.height(24.dp)) }

            // --- ALL ROUTINES ---
            item {
                SectionHeader("All routines")
                FilterBar()
            }
            items(6) { index ->
                ListItem(
                    headlineContent = { Text("Routine no. $index") },
                    supportingContent = { Text("Description for routine no. $index") },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = surfaceVariantLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
fun FilterBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(Icons.Default.SwapVert, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Filter")
        }
        FilledIconButton(
            onClick = { },
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = null
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