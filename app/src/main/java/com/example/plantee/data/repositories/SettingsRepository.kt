package com.example.plantee.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.plantee.domain.repositories.ISettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import androidx.core.content.edit

class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : ISettingsRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("settings_pref", Context.MODE_PRIVATE)

    override fun getNotificationsEnabled(): Flow<Boolean> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == NOTIFICATIONS_ENABLED_KEY) {
                trySend(prefs.getBoolean(NOTIFICATIONS_ENABLED_KEY, false))
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED_KEY, false))
        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.onStart { emit(sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED_KEY, false)) }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(NOTIFICATIONS_ENABLED_KEY, enabled) }
    }

    companion object {
        private const val NOTIFICATIONS_ENABLED_KEY = "notifications_enabled"
    }
}
