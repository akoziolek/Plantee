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
import com.example.plantee.ui.viewmodels.home.HomeEvent
import com.example.plantee.ui.viewmodels.plant.PlantEditEvent
import com.example.plantee.ui.viewmodels.plant.PlantsEvent
import com.example.plantee.ui.viewmodels.routine.RoutineAddEvent
import com.example.plantee.ui.viewmodels.routine.RoutineDetailsEvent
import com.example.plantee.ui.viewmodels.routine.RoutineEditEvent
import com.example.plantee.ui.viewmodels.routine.RoutinesEvent

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
                    onNavigate = { event ->
                        when(event) {
                            is HomeEvent.NavigateToRoutines -> {
                                viewModel.navigate(Screen.Routines)
                            }
                            is HomeEvent.NavigateToRoutine -> {
                                viewModel.navigate(Screen.RoutineDetails(event.routineId))
                            }
                            is HomeEvent.NavigateToPlant -> {
                                viewModel.navigate(Screen.PlantDetails(event.plantId))
                            }
                            HomeEvent.NavigateToPlantAdd -> {
                                viewModel.navigate(Screen.PlantAdd)
                            }
                        }
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
                            is PlantAddEvent.NavigateToDetails -> {
                                Log.d("MyLog", "NavigateToDetails with id: ${event.plantId}")
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
                    onNavigate = { event ->
                        when(event) {
                            is RoutinesEvent.NavigateToDetails -> {
                                viewModel.navigate(Screen.RoutineDetails(event.routineId))
                            }
                            is RoutinesEvent.NavigateToAdd -> {
                                viewModel.navigate(Screen.RoutineAdd)
                            }
                            RoutinesEvent.NavigateBack -> {
                                viewModel.back()
                            }
                        }
                    }
                )
            }

            entry<Screen.RoutineDetails> { route ->
                RoutineDetailsScreen (
                    routineId = route.routineId,
                    onNavigate = { event ->
                        when (event) {
                            is RoutineDetailsEvent.NavigateToPlant -> {
                                viewModel.navigate(Screen.PlantDetails(event.plantId))
                            }
                            is RoutineDetailsEvent.NavigateToEdit -> {
                                viewModel.navigate(Screen.RoutineEdit(event.routineId))
                            }
                            is RoutineDetailsEvent.RoutineDeleted -> {
                                viewModel.back()
                            }
                            RoutineDetailsEvent.NavigateBack -> {
                                viewModel.navigate(Screen.Routines)
                            }
                        }
                    }
                )
            }

            entry<Screen.RoutineAdd> {
                RoutineAddScreen(
                    onNavigate = { event ->
                        when(event) {
                            is RoutineAddEvent.NavigateToDetails -> {
                                viewModel.navigate(Screen.RoutineDetails(event.routineId))
                            }
                            RoutineAddEvent.NavigateBack -> {
                                viewModel.back()
                            }
                        }
                    }
                )
            }

            entry<Screen.RoutineEdit> { route ->
                RoutineEditScreen(
                    routineId = route.routineId,
                    onNavigate = { event ->
                        when(event) {
                            is RoutineEditEvent.RoutineUpdated -> {
                                viewModel.popUpTo(Screen.RoutineDetails(route.routineId), inclusive = false)
                            }
                            RoutineEditEvent.NavigateBack -> viewModel.back()
                        }
                    }
                )
            }
        }
    )
}
