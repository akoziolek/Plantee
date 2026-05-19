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
        media = media.toDomain(),
        routines = plantRoutines.map {
            RoutineSummary(
                id = it.id,
                name = it.name,
                description = it.description
            )
        }
    )
}

fun Diagnosis?.toEntity(): DiagnosisEntity? {
    if (this == null) return null

    return DiagnosisEntity(
        id = this.id,
        problemDescription = this.problemDescription,
        sunLevel = this.sunLevel,
        moistureLevel = this.moistureLevel,
        diagnosedAt = this.diagnosedAt,
        idPlant = this.plantId,
        idMedia = this.media?.id
    )
}