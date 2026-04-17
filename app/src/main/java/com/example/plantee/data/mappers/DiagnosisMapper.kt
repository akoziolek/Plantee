package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.FullDiagnosisEntity
import com.example.plantee.domain.model.Diagnosis
fun FullDiagnosisEntity?.toDomain(): Diagnosis? {
    if (this == null) return null

    return Diagnosis(
        id = diagnosis.id,
        description = diagnosis.description,
        sunLevel = diagnosis.sunLevel,
        moistureLevel = diagnosis.moistureLevel,
        diagnosedAt = diagnosis.diagnosedAt,
        listOfMedia = listOfMedia,
        routinesIds = plantRoutines.map { it.idRoutine }
    )
}

fun List<FullDiagnosisEntity>.toDomainList(): List<Diagnosis> {
    return this.map { entity ->
        Diagnosis(
            id = entity.diagnosis.id,
            description = entity.diagnosis.description,
            sunLevel = entity.diagnosis.sunLevel,
            moistureLevel = entity.diagnosis.moistureLevel,
            diagnosedAt = entity.diagnosis.diagnosedAt,
            listOfMedia = entity.listOfMedia,
            routinesIds = entity.plantRoutines.map { it.idRoutine }
        )
    }
}