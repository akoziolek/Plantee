package com.example.plantee.domain.repositories

import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun getNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
