package com.example.plantee.ui.nav

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
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
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosePlantEvent
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisDetailsEvent
import com.example.plantee.ui.viewmodels.diagnosis.DiagnosisResultsEvent
import com.example.plantee.ui.viewmodels.plant.PlantEditEvent
import com.example.plantee.ui.viewmodels.plant.PlantsEvent

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = viewModel()
) {

    NavDisplay(
        backStack = viewModel.backStack,
        onBack = { viewModel.back() },
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
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

            entry<Screen.DiagnosePlant> { route ->
                DiagnosePlantScreen(
                    plantId = route.plantId,
                    onNavigate = { event ->
                        when(event) {
                            is DiagnosePlantEvent.NavigateToDiagnosis -> {
                                viewModel.replace(Screen.DiagnosisResults(diagnosisId = event.diagnosisId))
                            }
                            DiagnosePlantEvent.NavigateBack -> {
                                viewModel.back()
                            }
                        }
                    }
                )
            }

            entry<Screen.DiagnosisDetails> { route ->
                DiagnosisDetailsScreen(
                    diagnosisId = route.diagnosisId,
                    onNavigate = { event ->
                        when (event) {
                            is DiagnosisDetailsEvent.NavigateToRoutine -> {
                                viewModel.replace(Screen.RoutineDetails(event.routineId))
                            }
                            DiagnosisDetailsEvent.NavigateBack -> {
                                viewModel.back()
                            }
                        }
                    }
                )
            }

            entry<Screen.DiagnosisResults> { route ->
                DiagnosisResultsScreen(
                    diagnosisId = route.diagnosisId,
                    onNavigate = { event ->
                        when(event) {
                            is DiagnosisResultsEvent.NavigateToRoutine -> {
                                viewModel.navigate(Screen.RoutineDetails(event.routineId))
                            }
                            is DiagnosisResultsEvent.NavigateBack -> {
                                viewModel.back()
                            }
                            is DiagnosisResultsEvent.FinishDiagnosis -> {
                                viewModel.replace(Screen.DiagnosisDetails(event.diagnosisId))
                            }

                        }

                    }
                )
            }

            entry<Screen.Plants> {
                PlantsScreen(
                    onNavigate = { event ->
                        when(event) {
                            is PlantsEvent.NavigateToDetails -> {
                                viewModel.navigate(Screen.PlantDetails(event.plantId))
                            }
                            is PlantsEvent.NavigateToAdd -> {
                                viewModel.navigate(Screen.PlantAdd)
                            }
                            PlantsEvent.NavigateBack -> {
                                viewModel.back()
                            }
                        }
                    }
                )
            }

            entry<Screen.PlantDetails> { route ->
                PlantDetailsScreen(
                    plantId = route.plantId,
                    onNavigate = { event ->
                        when (event) {
                            is PlantDetailsEvent.NavigateToDiagnose -> {
                                viewModel.navigate(Screen.DiagnosePlant(event.plantId))
                            }
                            is PlantDetailsEvent.NavigateToDiagnosis -> {
                                viewModel.navigate(Screen.DiagnosisDetails(event.diagnosisId))
                            }
                            is PlantDetailsEvent.NavigateToRoutine -> {
                                viewModel.navigate(Screen.RoutineDetails(event.routineId))
                            }
                            is PlantDetailsEvent.NavigateToEdit -> {
                                viewModel.navigate(Screen.PlantEdit(event.plantId))
                            }
                            is PlantDetailsEvent.PlantDeleted -> {
                                viewModel.back()
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
                            // TODO if 'Create first entry is enabled' route to AddEntry
                            is PlantAddEvent.NavigateToDetails -> {
                                viewModel.replace(Screen.PlantDetails(event.plantId))
                            }
                            PlantAddEvent.NavigateBack -> viewModel.back()
                        }
                    }
                )
            }

            entry<Screen.PlantEdit> { route ->
                PlantEditScreen(
                    plantId = route.plantId,
                    onNavigate = { event ->
                        when(event) {
                            is PlantEditEvent.PlantUpdated -> {
                                viewModel.popUpTo(Screen.PlantDetails(route.plantId), inclusive = false)
                            }
                            PlantEditEvent.NavigateBack -> viewModel.back()
                        }

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

            entry<Screen.RoutineDetails> { route ->
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

            entry<Screen.RoutineEdit> { route ->
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
