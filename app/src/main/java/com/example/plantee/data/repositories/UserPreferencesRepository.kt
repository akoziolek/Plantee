    package com.example.plantee.data.repositories

    import android.content.Context
    import androidx.datastore.core.DataStore
    import androidx.datastore.preferences.core.Preferences
    import androidx.datastore.preferences.core.booleanPreferencesKey
    import androidx.datastore.preferences.core.edit
    import androidx.datastore.preferences.preferencesDataStore
    import com.example.plantee.domain.repositories.IUserPreferencesRepository
    import dagger.hilt.android.qualifiers.ApplicationContext
    import kotlinx.coroutines.flow.Flow
    import kotlinx.coroutines.flow.map
    import javax.inject.Inject
    import javax.inject.Singleton

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    @Singleton
    class UserPreferencesRepository @Inject constructor(
        @ApplicationContext private val context: Context
    ) : IUserPreferencesRepository {

        companion object PreferencesKeys {
            private val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        }

        override val isDarkTheme: Flow<Boolean?> = context.dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME]
        }

        override suspend fun setDarkTheme(isDark: Boolean?) {
            context.dataStore.edit { preferences ->
                if (isDark == null) {
                    preferences.remove(IS_DARK_THEME)
                } else {
                    preferences[IS_DARK_THEME] = isDark
                }
            }
        }
    }
