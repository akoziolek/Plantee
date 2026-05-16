package com.example.plantee.data.mappers

import com.example.plantee.data.local.dto.RoutineSummaryDto
import com.example.plantee.data.local.entities.RoutineEntity
import com.example.plantee.data.local.relations.RoutineWithDetails
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
        plants = plants.toSummaryDomainList()
    )
}

fun List<RoutineSummaryDto>.toSummaryDomainList(): List<RoutineSummary> {
    return this.map { dto ->
        RoutineSummary(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            lastlyDoneAt = dto.lastlyDoneAt
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