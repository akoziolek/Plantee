package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.relations.DiagnosisWithDetails
import com.example.plantee.domain.model.Diagnosis
import com.example.plantee.domain.model.RoutineSummary

fun DiagnosisWithDetails?.toDomain(): Diagnosis? {
    if (this == null) return null

    return Diagnosis(
        id = diagnosis.id,
        problemDescription = diagnosis.problemDescription,
        response = diagnosis.response,
        sunLevel = diagnosis.sunLevel,
        moistureLevel = diagnosis.moistureLevel,
        diagnosedAt = diagnosis.diagnosedAt,
        plantId = diagnosis.idPlant,
        listOfMedia = listOfMedia.toDomainList(),
        routines = plantRoutines.map {
            RoutineSummary(
                id = it.id,
                name = it.name,
                description = it.description
            )
        }
    )
}

fun List<DiagnosisWithDetails>.toDomainList(): List<Diagnosis> {
    return this.map { entity ->
        Diagnosis(
            id = entity.diagnosis.id,
            problemDescription = entity.diagnosis.problemDescription,
            response = entity.diagnosis.response,
            sunLevel = entity.diagnosis.sunLevel,
            moistureLevel = entity.diagnosis.moistureLevel,
            diagnosedAt = entity.diagnosis.diagnosedAt,
            plantId = entity.diagnosis.idPlant,
            listOfMedia = entity.listOfMedia.toDomainList(),
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

fun Diagnosis?.toEntity(): DiagnosisEntity? {
    if (this == null) return null

    return DiagnosisEntity(
        id = this.id,
        problemDescription = this.problemDescription,
        sunLevel = this.sunLevel,
        moistureLevel = this.moistureLevel,
        diagnosedAt = this.diagnosedAt,
        idPlant = this.plantId
    )
}