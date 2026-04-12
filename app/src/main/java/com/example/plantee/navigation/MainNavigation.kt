package com.example.plantee.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plantee.ui.screens.diagnosis.DiagnosePlantScreen
import com.example.plantee.ui.screens.diagnosis.DiagnosisDetailsScreen
import com.example.plantee.ui.screens.diagnosis.DiagnosisResultsScreen
import com.example.plantee.ui.screens.home.HomeScreen
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
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination= Screen.Home) {
        composable<Screen.Home> {
            HomeScreen(
                onRoutineClick = { id -> navController.navigate(route = Screen.RoutineDetails) },
                onPlantClick = { id -> navController.navigate(route = Screen.PlantDetails) },
                onAddPlantClick = { navController.navigate(route = Screen.PlantAdd) },
                onRoutinesClick = { navController.navigate(route = Screen.Routines) }
            )
        }

        composable<Screen.DiagnosePlant> {
            DiagnosePlantScreen(
                onDiagnoseClick = { navController.navigate(Screen.DiagnosisResults) },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Screen.DiagnosisDetails> {
            DiagnosisDetailsScreen(
                onBackClick = { navController.navigateUp() },
                onRoutineClicked = { navController.navigate(Screen.RoutineDetails)}
            )
        }
        composable<Screen.DiagnosisResults> {
            DiagnosisResultsScreen(
                onFinishClick = {
                    navController.navigate(route = Screen.DiagnosisDetails) {
                        popUpTo<Screen.DiagnosisResults> { inclusive = true }
                    }
                },
                onBackClick = { navController.navigateUp() }
            )
        }

        composable<Screen.PlantAdd> {
            PlantAddScreen(
                onAddPlantClick = {
                    navController.navigate(route = Screen.PlantDetails) {
                        popUpTo<Screen.PlantAdd> { inclusive = true }
                    }
                },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Screen.PlantDetails> {
            PlantDetailsScreen(
                onDiagnoseClick = { navController.navigate( route = Screen.DiagnosePlant) },
                onConnectedRoutinesClick = { navController.navigate(route = Screen.Routines) },
                onDiagnosesClick = { navController.navigate(route = Screen.DiagnosisDetails) },
                onRoutineClick = { id -> navController.navigate(route = Screen.RoutineDetails) },
                onBackClick = { navController.navigateUp() },
                onDiagnosisClick = { id -> navController.navigate(route = Screen.DiagnosisDetails) }
            )
        }
        composable<Screen.PlantEdit> {
            PlantEditScreen(
                onSaveClick = {
                    navController.navigate(route = Screen.PlantDetails) {
                        popUpTo<Screen.PlantEdit> { inclusive = true }
                    }
                },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Screen.Plants> {
            PlantsScreen(
                onAddPlantClick = { navController.navigate(Screen.PlantAdd)}
            )
        }

        composable<Screen.RoutineAdd> {
            RoutineAddScreen(
                onAddRoutineClick = {
                    navController.navigate(route = Screen.RoutineDetails) {
                        popUpTo<Screen.RoutineAdd> { inclusive = true }
                    }
                },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Screen.RoutineDetails> {
            RoutineDetailsScreen(
                onPlantClick = {navController.navigate(route = Screen.PlantDetails)},
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Screen.RoutineEdit> {
            RoutineEditScreen(
                onSaveRoutineClick = {
                    navController.navigate(route = Screen.RoutineDetails) {
                        popUpTo<Screen.RoutineEdit> { inclusive = true }
                    }
                },
                onBackClick = { navController.navigateUp() }
            )
        }
        composable<Screen.Routines> {
            RoutinesScreen(
                onRoutineAddClick = { navController.navigate(Screen.RoutineAdd) },
                onRoutineClick = { id -> navController.navigate(route = Screen.RoutineDetails) }
            )
        }
    }
}