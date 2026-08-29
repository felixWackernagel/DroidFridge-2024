package de.wackernagel.droidfridge

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class Preferences(private val context: Context) {

    companion object {
        val SHOW_SAMPLE_CREATOR =
            booleanPreferencesKey("show_sample_creator")
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
}