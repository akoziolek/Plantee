package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.FullPlantEntity
import com.example.plantee.domain.model.Plant

fun FullPlantEntity?.toDomain(): Plant? {
    if (this == null) return null

    return Plant(
        id = plant.id,
        name = plant.name,
        description = plant.description,
        species = plant.species,
        place = plant.place,
        state = plant.state,
        isFavourite = plant.isFavourite,
        media = media?.toDomain(),
        diagnosesIds = plantDiagnoses.map { it.id },
        routinesIds = plantRoutines.map { it.idRoutine }
    )
}

fun List<FullPlantEntity>.toDomainList(): List<Plant> {
    return this.map { entity ->
        Plant(
            id = entity.plant.id,
            name = entity.plant.name,
            description = entity.plant.description,
            species = entity.plant.species,
            place = entity.plant.place,
            state = entity.plant.state,
            isFavourite = entity.plant.isFavourite,
            media = entity.media?.toDomain(),
            diagnosesIds = entity.plantDiagnoses.map { it.id },
            routinesIds = entity.plantRoutines.map { it.id }
        )
    }
}