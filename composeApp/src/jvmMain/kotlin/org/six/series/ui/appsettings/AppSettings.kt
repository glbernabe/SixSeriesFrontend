package org.six.series.ui.appsettings

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class AppSettings(databasePath: String) {

    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { databasePath.toPath().toFile() }
    )

    companion object {
        private val THEME_COLOR_KEY = longPreferencesKey("theme_color_hex")
        private val AVATAR_URL_KEY = stringPreferencesKey("avatar_url")
        private val PROFILE_NAME_KEY = stringPreferencesKey("profile_name")
        const val DEFAULT_COLOR = 0xFF6A6A69L
    }

    // ── Color ──
    val currentHexColor: Flow<Long> = dataStore.data
        .map { it[THEME_COLOR_KEY] ?: DEFAULT_COLOR }

    suspend fun updateColor(newColor: Long) {
        dataStore.edit { it[THEME_COLOR_KEY] = newColor }
    }

    // ── Avatar URL (local cache) ──
    val avatarUrl: Flow<String?> = dataStore.data
        .map { it[AVATAR_URL_KEY] }

    suspend fun updateAvatarUrl(url: String) {
        dataStore.edit { it[AVATAR_URL_KEY] = url }
    }

    // ── Profile name (local cache) ──
    val profileName: Flow<String?> = dataStore.data
        .map { it[PROFILE_NAME_KEY] }

    suspend fun updateProfileName(name: String) {
        dataStore.edit { it[PROFILE_NAME_KEY] = name }
    }
}
