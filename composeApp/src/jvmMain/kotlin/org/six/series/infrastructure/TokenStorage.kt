package org.six.series.infrastructure

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TokenStorage(private val settings: Settings) {
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    private val _userDataFlow = MutableStateFlow<UserTokenData?>(decodeCurrentUserData())
    val userDataFlow: StateFlow<UserTokenData?> = _userDataFlow.asStateFlow()

    fun saveTokens(accessToken: String, refreshToken: String) {
        settings.putString(KEY_ACCESS_TOKEN, accessToken)
        settings.putString(KEY_REFRESH_TOKEN, refreshToken)
        _userDataFlow.value = decodeCurrentUserData()
    }

    fun getAccessToken(): String? = settings.getStringOrNull(KEY_ACCESS_TOKEN)
    fun getRefreshToken(): String? = settings.getStringOrNull(KEY_REFRESH_TOKEN)

    private fun decodeCurrentUserData(): UserTokenData? {
        val token = getAccessToken() ?: return null
        return try {
            TokenJwt(token).getUserData()
        } catch (e: Exception) {
            null
        }
    }

    fun getUserData(): UserTokenData? = _userDataFlow.value

    fun clear() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
        _userDataFlow.value = null
    }
}