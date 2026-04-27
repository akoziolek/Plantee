package com.example.plantee.data.mappers

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
        mediaId = plant.idMedia,
        diagnoses = plantDiagnoses.map {
            DiagnosisSummary(
                id = it.id,
                diagnosedAt = it.diagnosedAt,
                description = it.problemDescription
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

fun PlantEntity?.toSummaryDomain(): PlantSummary? {
    if (this == null) return null

    return PlantSummary(
            id = this.id,
            name = this.name,
            description = this.description,
            isFavourite = this.isFavourite
        )
}

fun List<PlantWithDetails>.toDomainList(): List<Plant> {
    return this.mapNotNull { it.toDomain() }
}

fun List<PlantEntity>.toSummaryDomainList(): List<PlantSummary> {
    return this.map { entity ->
        PlantSummary(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            isFavourite = entity.isFavourite
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
        idMedia = this.mediaId
    )
}
