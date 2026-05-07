package org.six.series.ui.appsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.six.series.AppRoute
import org.six.series.application.dto.RefreshDto
import org.six.series.infrastructure.TokenJwt
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.six.series.infrastructure.TokenStorage

class AppViewModel(
    private val settings: AppSettings,
    private val tokenStorage: TokenStorage,
    private val client: HttpClient
) : ViewModel() {

    // The UI observes and changes automatically
    val currentHexColor = settings.currentHexColor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings.DEFAULT_COLOR
    )

    // This function is to change the color of the pfp
    fun updateAppColor(colorHex: Long) {
        viewModelScope.launch {
            settings.updateColor(colorHex)
        }
    }
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val access = tokenStorage.getAccessToken()
            val refresh = tokenStorage.getRefreshToken()

            if (!access.isNullOrEmpty()) {
                val token = TokenJwt(access)
                if (token.isSessionValid()) {
                    // Valid Token
                    _startDestination.value = AppRoute.Main
                } else if (!refresh.isNullOrEmpty()) {
                    // Access token expired -> Refresh
                    val newTokens = tryRefreshToken(refresh)
                    if (newTokens != null) {
                        tokenStorage.saveTokens(newTokens.access_token!!, newTokens.refresh_token!!)
                        _startDestination.value = AppRoute.Main
                    } else {
                        // Refresh token expired -> Login again
                        _startDestination.value = AppRoute.Login
                    }
                } else {
                    _startDestination.value = AppRoute.Login
                }
            } else {
                _startDestination.value = AppRoute.Login
            }
        }
    }
    // Call to the endpoint to refresh
    private suspend fun tryRefreshToken(refreshToken: String): RefreshDto? {
        return try {
            client.post("http://localhost:8080/api/public/refresh") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("refresh_token" to refreshToken))
            }.body<RefreshDto>()
        } catch (e: Exception) {
            null
        }
    }
}