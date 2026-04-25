package com.example.plantee.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data object DiagnosePlant : Screen
    @Serializable
    data class DiagnosisDetails(val id: Int) : Screen
    @Serializable
    data class DiagnosisResults(val id: Int) : Screen

    @Serializable
    data object PlantAdd : Screen
    @Serializable
    data class PlantDetails(val id: Int) : Screen
    @Serializable
    data class PlantEdit(val id: Int): Screen
    @Serializable
    data object Plants : Screen


    @Serializable
    data object RoutineAdd : Screen
    @Serializable
    data class RoutineDetails(val id: Int) : Screen
    @Serializable
    data class RoutineEdit(val id: Int) : Screen
    @Serializable
    data object Routines : Screen


}