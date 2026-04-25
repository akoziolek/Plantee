package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.plantee.R
import com.example.plantee.ui.nav.Screen
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.theme.extendedLight
import com.example.plantee.ui.theme.titleLargeBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BaseTopBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    title: String,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    BaseTopBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
            }
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    BaseTopBar(
        title = { Text(title, style = MaterialTheme.typography.titleLargeBold) },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    state: SearchBarState,
    modifier: Modifier = Modifier,
    placeholder: String = "Search"
) {
    SearchBar(
        state = state,
        inputField = {
            InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                placeholder = { Text(placeholder) },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .safeDrawingPadding(),
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = SearchBarDefaults.inputFieldShape,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
    )
}

sealed class NavItem(val title: String, val icon: ImageVector, val screen: Screen) {
    object Home : NavItem(title = "Home", Icons.Default.Home, screen = Screen.Home)
    object Plants : NavItem(title = "Plants", Icons.Default.LocalFlorist, screen = Screen.Plants)
    object Routines : NavItem(title = "Routines", Icons.Default.CalendarMonth, screen = Screen.Routines)
}
@Composable
fun NavBar(
    currentScreen: Screen?,
    onTabSelected: (Screen) -> Unit
) {
    val items = listOf(NavItem.Home, NavItem.Plants, NavItem.Routines)

    NavigationBar(
        containerColor = extendedLight.dimNeutral.colorContainer,
        tonalElevation = 3.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen

            NavigationBarItem(
                selected =  isSelected,
                onClick = {
                    onTabSelected(item.screen)
                },
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

@Composable
fun FilterBar(
    onFilterClick: () -> Unit,
    onViewModeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onFilterClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
            contentPadding = PaddingValues(
                start = 0.dp,
                top = 8.dp,
                end = 12.dp,
                bottom = 8.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = null,
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.filter_menu_label),
                style = MaterialTheme.typography.labelLarge
            )
        }

        IconButton(
            onClick = onViewModeClick,
        ) {
            Icon(
                Icons.AutoMirrored.Filled.List,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopBarsPreview() {
    PlanteeTheme {
        Column {
            BackTopBar(stringResource(R.string.plant_add_title), onBackClick = { })
            BackTopBar(stringResource(R.string.plant_edit_title), onBackClick = { })
            BackTopBar(stringResource(R.string.diagnosis_form_title), onBackClick = { })
            BackTopBar(stringResource(R.string.diagnosis_results_title), onBackClick = { })
            BackTopBar(stringResource(R.string.routine_add_title), onBackClick = { })
            BackTopBar(stringResource(R.string.routine_edit_title), onBackClick = { })

            BackTopBar(stringResource(R.string.routine_details_title), onBackClick = { }, actions = {
                IconButton(onClick = { /* action 1 */ }) {
                    Icon(Icons.Default.BookmarkBorder, "Add to favourites")
                }
                IconButton(onClick = { /* action 2 */ }) {
                    Icon(Icons.Default.MoreVert, "See more")
                }
            })
            BackTopBar(stringResource(R.string.plant_details_title), onBackClick = { }, actions = {
                IconButton(onClick = { /* action 1 */ }) {
                    Icon(Icons.Default.BookmarkBorder, "Add to favourites")
                }
                IconButton(onClick = { /* action 2 */ }) {
                    Icon(Icons.Default.MoreVert, "See more")
                }
            })

            MainTopBar(stringResource(R.string.home_title), actions = {
                IconButton(onClick = { /* action 1 */ }) {
                    Icon(Icons.Default.NotificationsNone, "Notifications")
                }
                IconButton(onClick = { /* action 2 */ }) {
                    Icon(Icons.Default.Settings, "Settings")
                }
            })

            SimpleSearchBar(
                query = "Our favourite plant",
                onQueryChange = {  },
                state = rememberSearchBarState(),
                placeholder = "Search for a plant",
                onSearch = { },
                expanded = false,
                onExpandedChange = { }
            )
            FilterBar({}, {})
            //NavBar()

        }

    }

}