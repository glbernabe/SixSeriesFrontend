package org.six.series.ui.appsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.six.series.AppRoute
import org.six.series.application.dto.RefreshDto
import org.six.series.infrastructure.TokenJwt
import ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings.AppSettings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.six.series.infrastructure.TokenStorage

class AppViewModel(
    private val settings: AppSettings,
    private val tokenStorage: TokenStorage,
    private val client: HttpClient
) : ViewModel() {

    val isDarkMode = settings.isDarkMode

    // Esto es para saber a donde hay que reindicar al usuario
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
                    // Token válido → ir a org.six.series.main
                    _startDestination.value = AppRoute.main
                } else if (!refresh.isNullOrEmpty()) {
                    // Token expirado → intentar refrescar
                    val newTokens = tryRefreshToken(refresh)
                    if (newTokens != null) {
                        tokenStorage.saveTokens(newTokens.access_token!!, newTokens.refresh_token!!)
                        _startDestination.value = AppRoute.main
                    } else {
                        // No se pudo refrescar → ir a login
                        _startDestination.value = AppRoute.login
                    }
                } else {
                    _startDestination.value = AppRoute.login
                }
            } else {
                _startDestination.value = AppRoute.login
            }
        }
    }
    // Función para llamar al endpoint de refresh
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

    // Opciones de los temas
    fun toggleTheme() = settings.toggleDarkMode()
    fun setDarkMode() = settings.setDarkMode()
    fun setLightMode() = settings.setLightMode()
    fun switchMode() = settings.toggleDarkMode()
}