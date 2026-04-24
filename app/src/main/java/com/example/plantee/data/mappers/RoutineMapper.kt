package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.FullRoutineEntity
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.domain.model.Routine

fun FullRoutineEntity?.toDomain(): Routine? {
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

fun List<FullRoutineEntity>.toDomainList(): List<Routine> {
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