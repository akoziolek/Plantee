package com.example.plantee.ui.screens.routine_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.theme.extendedLight

sealed class NavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : NavItem("Home", Icons.Default.Home, "home")
    object Plants : NavItem("Plants", Icons.Default.LocalFlorist, "plants")
    object Routines : NavItem("Routines", Icons.Default.CalendarMonth, "routines")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailsScreen() {
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)

    Scaffold(
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("The rest of the chaos")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoutineDetailsPreview() {
    PlanteeTheme {
        RoutineDetailsScreen()
    }
}