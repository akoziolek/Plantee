package com.example.plantee.data.mappers

import com.example.plantee.data.local.entities.DiagnosisEntity
import com.example.plantee.data.local.relations.DiagnosisWithDetails
import com.example.plantee.domain.model.Diagnosis
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
        listOfMedia = listOfMedia.map { it.id },
        routinesIds = plantRoutines.map { it.idRoutine }
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
            listOfMedia = entity.listOfMedia.map { it.id },
            routinesIds = entity.plantRoutines.map { it.idRoutine }
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