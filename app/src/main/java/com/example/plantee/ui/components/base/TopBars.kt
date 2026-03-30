package com.example.plantee.ui.components.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.plantee.R
import com.example.plantee.ui.theme.PlanteeTheme
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
fun SearchBar() {
    // TO DO
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TopBarsPreview() {
    PlanteeTheme {
        Column {
            //TODO move CONTENT DESCRIPTIONS to strings.xml??
            BackTopBar(stringResource(R.string.plant_add_title), onBackClick = { })
            BackTopBar(stringResource(R.string.plant_edit_title), onBackClick = { })
            BackTopBar(stringResource(R.string.diagnosis_form_title), onBackClick = { })
            BackTopBar(stringResource(R.string.diagnosis_results_title), onBackClick = { })
            BackTopBar(stringResource(R.string.routines_add_title), onBackClick = { })
            BackTopBar(stringResource(R.string.routines_edit_title), onBackClick = { })

            BackTopBar(stringResource(R.string.routines_details_title), onBackClick = { }, actions = {
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
        }

    }

}