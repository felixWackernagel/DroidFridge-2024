package de.wackernagel.droidfridge

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class Preferences(
    private val context: Context
) {

    companion object {
        val SHOW_SAMPLE_CREATOR =
            booleanPreferencesKey("show_sample_creator")
        val IS_FIRST_START = booleanPreferencesKey("first_start")
        val LAST_VERSION_CODE = intPreferencesKey("last_version_code")
    }

    val showSampleCreator: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[SHOW_SAMPLE_CREATOR] ?: true
        }

    suspend fun setShowSampleCreator(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_SAMPLE_CREATOR] = value
        }
    }

    val isFirstStart: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[IS_FIRST_START] ?: true
        }

    suspend fun setFirstStart(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_START] = value
        }
    }

    val lastVersionCode: Flow<Int> =
        context.dataStore.data.map { preferences ->
            preferences[LAST_VERSION_CODE] ?: -1
        }

    suspend fun getLastVersionCode(): Int = lastVersionCode.first()

    suspend fun setLastVersionCode(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAST_VERSION_CODE] = value
        }
    }
}