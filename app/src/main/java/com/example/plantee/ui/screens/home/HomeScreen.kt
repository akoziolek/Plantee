package com.example.plantee.ui.screens.home

import androidx.appcompat.app.AppCompatDelegate
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.plantee.R
import com.example.plantee.ui.components.base.MainTopBar
import com.example.plantee.ui.components.base.OverflowAction
import com.example.plantee.ui.components.base.OverflowMenu
import com.example.plantee.ui.components.base.PrimaryFloatingButton
import com.example.plantee.ui.components.base.StreakWidget
import com.example.plantee.ui.components.shared.CelebrationWrapper
import com.example.plantee.ui.components.shared.FilterSectionHeader
import com.example.plantee.ui.components.shared.LinkHeader
import com.example.plantee.ui.components.shared.plantListItems
import com.example.plantee.ui.components.shared.todayRoutinesSection
import com.example.plantee.ui.theme.PlanteeTheme
import com.example.plantee.ui.viewmodels.home.HomeEvent
import com.example.plantee.ui.viewmodels.home.HomeViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>(),
    onNavigate: (HomeEvent) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sort by viewModel.sortOrder.collectAsStateWithLifecycle()
    val isDarkThemePref by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val isDark = isDarkThemePref ?: isSystemInDarkTheme()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasNotificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                } else {
                    true
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            viewModel.setNotificationsEnabled(true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                HomeEvent.RequestNotificationPermission -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.setNotificationsEnabled(true)
                    }
                }
                else -> onNavigate(event)
            }
        }
    }

    Scaffold(
        topBar = {
            MainTopBar(
                title = stringResource(R.string.home_title),
                actions = {
                    IconButton(onClick = { viewModel.onNotificationIconClick(hasNotificationPermission) }) {
                        val icon = if (state.isNotificationsEnabled && hasNotificationPermission) {
                            Icons.Default.NotificationsActive
                        } else {
                            Icons.Default.NotificationsOff
                        }
                        Icon(icon, "Notifications")
                    }

                    val currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags()

                    OverflowMenu(
                        actions = listOf(
                            OverflowAction(
                                text =
                                    if (isDark)
                                        stringResource(R.string.home_menu_change_mode_light)
                                    else stringResource(R.string.home_menu_change_mode_dark),
                                onClick = { viewModel.toggleTheme(isDark) }
                            ),
                            OverflowAction(
                                text = "English",
                                icon = if (currentLang == "en") Icons.Default.Check else null,
                                onClick = { viewModel.changeLanguage("en") }
                            ),
                            OverflowAction(
                                text = "Polski",
                                icon = if (currentLang == "pl") Icons.Default.Check else null,
                                onClick = { viewModel.changeLanguage("pl") }
                            )
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            PrimaryFloatingButton(text = stringResource(R.string.home_btn_add), onClick = { viewModel.onAddPlantClick() })
        }
    ) { innerPadding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TODO Delete debug info when not needed
                item {
                    Text(
                        text = "DEBUG: LogicEnabled=${state.isNotificationsEnabled}, SysPermission=$hasNotificationPermission",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                    item {
                        StreakWidget(
                            streakDays = state.streakDays,
                            progress = state.streakProgress

                        )
                    }
                    item {
                        LinkHeader(
                            title = stringResource(R.string.home_label_today_routines),
                            onClick = { viewModel.onRoutinesClick() }
                        )
                    }

                    // FIXME - routines that are out of date range should not be displayed!!!!
                    todayRoutinesSection(
                        routines = state.todayRoutines,
                        onCheckboxClick = { viewModel.onCheckboxClick(it) },
                        onItemClick = { viewModel.onRoutineClick(it) }
                    )

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    item {
                        FilterSectionHeader(
                            title = stringResource(R.string.plants_label_your_plants),
                            filterTitle = stringResource(R.string.home_label_filter_plants),
                            onClick = { viewModel.toggleSortOrder() },
                            sort = sort
                        )
                    }

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
fun HomeScreenPreview() {
    PlanteeTheme {
        HomeScreen(
            onNavigate = {},
        )
    }
}
