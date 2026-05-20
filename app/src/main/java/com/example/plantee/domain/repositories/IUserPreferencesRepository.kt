package com.example.plantee.domain.repositories

import com.example.plantee.utils.AppTheme
import kotlinx.coroutines.flow.Flow

interface IUserPreferencesRepository {
    val theme: Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}
