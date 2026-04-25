package com.example.plantee.ui.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.plantee.ui.screens.diagnosis.DiagnosePlantScreen
import com.example.plantee.ui.screens.diagnosis.DiagnosisDetailsScreen
import com.example.plantee.ui.screens.diagnosis.DiagnosisResultsScreen
import com.example.plantee.ui.screens.home.HomeScreen
import com.example.plantee.ui.viewmodels.plant.PlantAddEvent
import com.example.plantee.ui.viewmodels.plant.PlantDetailsEvent
import com.example.plantee.ui.screens.plant.PlantAddScreen
import com.example.plantee.ui.screens.plant.PlantDetailsScreen
import com.example.plantee.ui.screens.plant.PlantEditScreen
import com.example.plantee.ui.screens.plant.PlantsScreen
import com.example.plantee.ui.screens.routine.RoutineAddScreen
import com.example.plantee.ui.screens.routine.RoutineDetailsScreen
import com.example.plantee.ui.screens.routine.RoutineEditScreen
import com.example.plantee.ui.screens.routine.RoutinesScreen


@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = viewModel()
) {

    NavDisplay(
        backStack = viewModel.backStack,
        onBack = { viewModel.back() },
        modifier = modifier,
        entryProvider = entryProvider {

            entry<Screen.Home> {
                HomeScreen(
                    onRoutineClick = { id ->
                        viewModel.navigate(Screen.RoutineDetails(id))
                    },
                    onPlantClick = { id ->
                        viewModel.navigate(Screen.PlantDetails(id))
                    },
                    onAddPlantClick = {
                        viewModel.navigate(Screen.PlantAdd)
                    },
                    onRoutinesClick = {
                        viewModel.navigate(Screen.Routines)
                    }
                )

            }

            entry<Screen.DiagnosePlant> {
                DiagnosePlantScreen(
                    onDiagnoseClick = {
                        viewModel.navigate(Screen.DiagnosisResults(1))
                    },
                    onBackClick = {
                        viewModel.back()
                    }
                )
            }

            entry<Screen.DiagnosisDetails> {
                DiagnosisDetailsScreen(
                    onRoutineClicked = { id ->
                        viewModel.navigate(Screen.RoutineDetails(id))
                    },
                    onBackClick = {
                        viewModel.back()
                    }
                )
            }

            entry<Screen.DiagnosisResults> {
                DiagnosisResultsScreen(
                    onFinishClick = {
                        viewModel.popUpTo(target = Screen.DiagnosePlant, inclusive = true)
                    },
                    onBackClick = {
                        viewModel.back()
                    },
                    onRoutineClick = { id ->
                        viewModel.navigate(Screen.RoutineDetails(id))
                    }
                )
            }

            entry<Screen.Plants> {
                PlantsScreen(
                    onPlantClick = {
                        viewModel.navigate(Screen.PlantDetails(it))
                    },
                    onAddPlantClick = {
                        viewModel.navigate(Screen.PlantAdd)
                    }
                )
            }

            entry<Screen.PlantDetails> { route ->
                PlantDetailsScreen(
                    plantId = route.plantId,
                    onNavigate = { event ->
                        when (event) {
                            is PlantDetailsEvent.NavigateToDiagnose -> {
                                viewModel.navigate(Screen.DiagnosePlant)
                            }
                            is PlantDetailsEvent.NavigateToDiagnosis -> {
                                viewModel.navigate(Screen.DiagnosisDetails(event.id))
                            }
                            is PlantDetailsEvent.NavigateToRoutine -> {
                                viewModel.navigate(Screen.RoutineDetails(event.id))
                            }
                            PlantDetailsEvent.NavigateBack -> {
                                viewModel.back()
                            }
                        }
                    }
                )
            }

            entry<Screen.PlantAdd> {
                PlantAddScreen(
                    onNavigate = { event ->
                        when (event) {
                            is PlantAddEvent.NavigateToDetails -> {
                                Log.d("MyLog", "NavigateToDetails with id: ${event.plantId}")
                                viewModel.replace(Screen.PlantDetails(event.plantId))
                            }
                            PlantAddEvent.NavigateBack -> viewModel.back()
                        }
                    }
                )
            }

            entry<Screen.PlantEdit> {
                PlantEditScreen(
                    onSaveClick = {
                        viewModel.replace(Screen.PlantDetails(1))
                    }, onBackClick = {
                        viewModel.back()
                    }
                )
            }

            entry<Screen.Routines> {
                RoutinesScreen(
                    onRoutineClick = { id ->
                        viewModel.navigate(Screen.RoutineDetails(id))
                    },
                    onRoutineAddClick = {
                        viewModel.navigate(Screen.RoutineAdd)
                    }
                )
            }

            entry<Screen.RoutineDetails> {
                RoutineDetailsScreen (
                    onPlantClick = { id ->
                        viewModel.navigate(Screen.PlantDetails(id))
                    },
                    onBackClick = {
                        viewModel.back()
                    }
                )
            }

            entry<Screen.RoutineAdd> {
                RoutineAddScreen(
                    onAddRoutineClick = {
                        viewModel.replace(Screen.RoutineDetails(1))
                    },
                    onBackClick = {
                        viewModel.back()
                    }
                )
            }

            entry<Screen.RoutineEdit> {
                RoutineEditScreen(
                    onSaveRoutineClick = {},
                    onBackClick = {
                        viewModel.back()
                    }
                )
            }
        }
    )
}