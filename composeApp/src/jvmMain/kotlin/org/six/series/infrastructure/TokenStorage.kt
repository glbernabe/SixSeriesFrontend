package org.six.series.infrastructure

import com.russhwolf.settings.Settings

class TokenStorage(private val settings: Settings) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        settings.putString(KEY_ACCESS_TOKEN, accessToken)
        settings.putString(KEY_REFRESH_TOKEN, refreshToken)
    }

    fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)
    fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)

    /**
     * Decodes the currently stored access token and extracts the structured user data.
     * Returns null if no token is found or if the token format is invalid.
     */
    fun getUserData(): UserTokenData? {
        val token = getAccessToken() ?: return null
        return try {
            TokenJwt(token).getUserData()
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }
}