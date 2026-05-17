package com.example.plantee.ui.nav

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisInput(
    val plantId: Long,
    val moistureLevel: Float,
    val sunLevel: Float,
    val problemDescription: String,
    val imageUri: String?
)

sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data class DiagnosePlant(val plantId: Long, val initialInput: DiagnosisInput? = null) : Screen
    @Serializable
    data class DiagnosisDetails(val diagnosisId: Long) : Screen
    @Serializable
    data class DiagnosisResults(val input: DiagnosisInput) : Screen

    @Serializable
    data object PlantAdd : Screen
    @Serializable
    data class PlantDetails(val plantId: Long) : Screen
    @Serializable
    data class PlantEdit(val plantId: Long): Screen
    @Serializable
    data object Plants : Screen


    @Serializable
    data object RoutineAdd : Screen
    @Serializable
    data class RoutineDetails(val routineId: Long) : Screen
    @Serializable
    data class RoutineEdit(val routineId: Long) : Screen
    @Serializable
    data object Routines : Screen


}