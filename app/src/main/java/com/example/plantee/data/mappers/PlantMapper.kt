package com.example.plantee.data.mappers

import com.example.plantee.data.local.dto.PlantSummaryDto
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.data.local.relations.PlantWithDetails
import com.example.plantee.domain.model.DiagnosisSummary
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.PlantSummary
import com.example.plantee.domain.model.RoutineSummary

fun PlantWithDetails?.toDomain(): Plant? {
    if (this == null) return null

    return Plant(
        id = plant.id,
        name = plant.name,
        description = plant.description,
        species = plant.species,
        state = plant.state,
        isFavourite = plant.isFavourite,
        media = media.toDomain(),
        diagnoses = plantDiagnoses.map {
            DiagnosisSummary(
                id = it.id,
                diagnosedAt = it.diagnosedAt,
                description = it.problemDescription,
                media = it.media.toDomain()
            )
        },
        routines = plantRoutines.map {
            RoutineSummary(
                id = it.id,
                name = it.name,
                description = it.description
            )
        }
    )
}

fun List<PlantSummaryDto>.toSummaryDomainList(): List<PlantSummary> {
    return this.map { dto ->
        PlantSummary(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            isFavourite = dto.isFavourite,
            media = dto.media.toDomain()
        )
    }
}

fun Plant?.toEntity(): PlantEntity? {
    if (this == null) return null

    return PlantEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        species = this.species,
        state = this.state,
        isFavourite = this.isFavourite,
        idMedia = this.media?.id
    )
}
