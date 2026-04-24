package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.DiagnosisEntity
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
        plantId = diagnosis.idPlant,
        listOfMedia = listOfMedia.map { it.id },
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
            plantId = entity.diagnosis.idPlant,
            listOfMedia = entity.listOfMedia.map { it.id },
            routinesIds = entity.plantRoutines.map { it.idRoutine }
        )
    }
}

fun Diagnosis?.toEntity(): DiagnosisEntity? {
    if (this == null) return null

    return DiagnosisEntity(
        id = this.id,
        description = this.description,
        sunLevel = this.sunLevel,
        moistureLevel = this.moistureLevel,
        diagnosedAt = this.diagnosedAt,
        idPlant = this.plantId
    )
}