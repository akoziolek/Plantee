package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.relations.RoutineWithDetails
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.Routine
import com.example.plantee.domain.model.RoutineSummary

fun RoutineWithDetails?.toDomain(): Routine? {
    if (this == null) return null

    return Routine(
        id = routine.id,
        name = routine.name,
        description = routine.description,
        startDate = routine.startDate,
        endDate = routine.endDate,
        activeDays = routine.activeDays,
        lastlyDoneAt = routine.lastlyDoneAt,
        diagnosisId = this.idDiagnosis,
        plantsIds = plantRoutines.map { it.idPlant }
    )
}

fun List<RoutineWithDetails>.toDomainList(): List<Routine> {
    return this.map { entity ->
        Routine(
            id = entity.routine.id,
            name = entity.routine.name,
            description = entity.routine.description,
            startDate = entity.routine.startDate,
            endDate = entity.routine.endDate,
            activeDays = entity.routine.activeDays,
            lastlyDoneAt = entity.routine.lastlyDoneAt,
            diagnosisId = entity.idDiagnosis,
            plantsIds = entity.plantRoutines.map { it.idPlant }
        )
    }
}

fun List<RoutineEntity>.toDomainListSimple(): List<Routine> {
    return this.map { entity ->
        Routine(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            startDate = entity.startDate,
            endDate = entity.endDate,
            activeDays = entity.activeDays,
            lastlyDoneAt = entity.lastlyDoneAt
        )
    }
}

fun List<RoutineEntity>.toSummaryDomainList(): List<RoutineSummary> {
    return this.map { entity ->
        RoutineSummary(
            id = entity.id,
            name = entity.name,
            description = entity.description
        )
    }
}

fun Routine?.toEntity(): RoutineEntity? {
    if (this == null) return null

    return RoutineEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        startDate = this.startDate,
        endDate = this.endDate,
        activeDays = this.activeDays,
        lastlyDoneAt = this.lastlyDoneAt
    )
}