    package com.example.plantee.data.repositories

    import android.content.Context
    import androidx.datastore.core.DataStore
    import androidx.datastore.preferences.core.Preferences
    import androidx.datastore.preferences.core.edit
    import androidx.datastore.preferences.core.stringPreferencesKey
    import androidx.datastore.preferences.preferencesDataStore
    import com.example.plantee.domain.repositories.IUserPreferencesRepository
    import com.example.plantee.utils.AppTheme
    import dagger.hilt.android.qualifiers.ApplicationContext
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.map
    import javax.inject.Inject
    import javax.inject.Singleton

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Singleton
    class UserPreferencesRepository @Inject constructor(
        @param:ApplicationContext private val context: Context
    ) : IUserPreferencesRepository {
        companion object {
            private val THEME_KEY = stringPreferencesKey("theme_preference")
        }

        override val theme: Flow<AppTheme> = context.dataStore.data.map { preferences ->
            val themeName = preferences[THEME_KEY] ?: AppTheme.SYSTEM.name
            AppTheme.valueOf(themeName)
        }

        override suspend fun setTheme(theme: AppTheme) {
            context.dataStore.edit { preferences ->
                preferences[THEME_KEY] = theme.name
            }
        }
    }
