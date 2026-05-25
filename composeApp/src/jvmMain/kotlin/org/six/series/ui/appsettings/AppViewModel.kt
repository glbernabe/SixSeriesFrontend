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

    val appColor = settings.currentHexColor.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings.DEFAULT_COLOR
    )

    fun updateAppColor(colorHex: Long) {
        viewModelScope.launch {
            settings.updateColor(colorHex)
        }
    }

    fun setColorFromProfile(colorLong: Long) {
        updateAppColor(colorLong)
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
                    _startDestination.value = AppRoute.ProfileSelector
                } else if (!refresh.isNullOrEmpty()) {
                    val newTokens = tryRefreshToken(refresh)
                    if (newTokens != null) {
                        tokenStorage.saveTokens(newTokens.access_token!!, newTokens.refresh_token!!)
                        _startDestination.value = AppRoute.ProfileSelector
                    } else {
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