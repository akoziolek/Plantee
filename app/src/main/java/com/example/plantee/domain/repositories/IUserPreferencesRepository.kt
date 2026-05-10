package com.example.plantee.domain.repositories

import kotlinx.coroutines.flow.Flow

interface IUserPreferencesRepository {
    val isDarkTheme: Flow<Boolean?>
    suspend fun setDarkTheme(isDark: Boolean?)
}
