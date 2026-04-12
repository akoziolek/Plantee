package com.example.plantee.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data object DiagnosePlant : Screen
    @Serializable
    data object DiagnosisDetails : Screen
    @Serializable
    data object DiagnosisResults : Screen

    @Serializable
    data object PlantAdd : Screen
    @Serializable
    data object PlantDetails : Screen
    @Serializable
    data object PlantEdit: Screen
    @Serializable
    data object Plants : Screen


    @Serializable
    data object RoutineAdd : Screen
    @Serializable
    data object RoutineDetails : Screen
    @Serializable
    data object RoutineEdit : Screen
    @Serializable
    data object Routines : Screen


}