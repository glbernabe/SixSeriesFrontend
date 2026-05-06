package org.six.series.ui.appsettings

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class AppSettings(databasePath: String) {

    // DataStore manages and keeps all the configurations
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { databasePath.toPath().toFile() }
    )

    companion object {
        private val THEME_COLOR_KEY = longPreferencesKey("theme_color_hex")
        const val DEFAULT_COLOR = 0xFF6A6A69L // Gray is by default
    }

    // With flows makes the change of the color responsive
    val currentHexColor: Flow<Long> = dataStore.data
        .map { preferences ->
            preferences[THEME_COLOR_KEY] ?: DEFAULT_COLOR
        }

    // Updates the color directly on the BINARY file
    suspend fun updateColor(newColor: Long) {
        dataStore.edit { preferences ->
            preferences[THEME_COLOR_KEY] = newColor
        }
    }
}