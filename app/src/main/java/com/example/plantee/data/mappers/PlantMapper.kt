package com.example.plantee.data.mappers

import com.example.plantee.data.local.relations.PlantWithDetails
import com.example.plantee.data.local.entities.PlantEntity
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.DiagnosisSummary
import com.example.plantee.domain.model.Plant
import com.example.plantee.domain.model.RoutineSummary

fun PlantWithDetails?.toDomain(): Plant? {
    if (this == null) return null

    return Plant(
        id = plant.id,
        name = plant.name,
        description = plant.description,
        species = plant.species,
        place = plant.place,
        state = plant.state,
        isFavourite = plant.isFavourite,
        mediaId = plant.idMedia,
        diagnoses = plantDiagnoses.map {
            DiagnosisSummary(
                id = it.id,
                diagnosedAt = it.diagnosedAt,
                description = it.description
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

fun List<PlantWithDetails>.toDomainList(): List<Plant> {
    return this.map { entity ->
        Plant(
            id = entity.plant.id,
            name = entity.plant.name,
            description = entity.plant.description,
            species = entity.plant.species,
            place = entity.plant.place,
            state = entity.plant.state,
            isFavourite = entity.plant.isFavourite,
            mediaId = entity.plant.idMedia,
            diagnoses = entity.plantDiagnoses.map {
                DiagnosisSummary(
                    id = it.id,
                    diagnosedAt = it.diagnosedAt,
                    description = it.description
                )
            },
            routines = entity.plantRoutines.map {
                RoutineSummary(
                    id = it.id,
                    name = it.name,
                    description = it.description
                )
            }
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
        place = this.place,
        state = this.state,
        isFavourite = this.isFavourite,
        idMedia = this.mediaId
    )
}