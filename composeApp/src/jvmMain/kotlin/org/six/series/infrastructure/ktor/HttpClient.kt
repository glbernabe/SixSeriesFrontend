package org.six.series.infrastructure.ktor

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO

import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens

import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation

import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.six.series.infrastructure.TokenStorage

fun createHttpClient(tokenStorage: TokenStorage, refreshUrl: String): HttpClient {
    return HttpClient(CIO) {

        // Header
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        // Loggin
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("KTOR CLIENT LOG: $message")
                }
            }
            level = LogLevel.ALL
        }

        // Json
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }

        // Auth
        install(Auth) {
            bearer {
                sendWithoutRequest { true }

                // Cargar tokens desde TokenStorage
                loadTokens {
                    val access = tokenStorage.getAccessToken()
                    val refresh = tokenStorage.getRefreshToken()
                    if (!access.isNullOrEmpty() && !refresh.isNullOrEmpty()) {
                        BearerTokens(access, refresh)
                    } else null
                }

                // Refrescar tokens automáticamente ante 401
                refreshTokens {
                    val refreshToken = tokenStorage.getRefreshToken() ?: return@refreshTokens null

                    try {
                        val response = client.post(refreshUrl) {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(mapOf("refresh_token" to refreshToken))
                        }

                        if (response.status == HttpStatusCode.OK) {
                            val data = response.body<Map<String, String>>()
                            val newAccess = data["access_token"] ?: ""
                            val newRefresh = data["refresh_token"] ?: refreshToken

                            tokenStorage.saveTokens(newAccess, newRefresh)

                            BearerTokens(newAccess, newRefresh)
                        } else {
                            tokenStorage.clear()
                            null
                        }
                    } catch (e: Exception) {
                        tokenStorage.clear()
                        null
                    }
                }
            }
        }

        // --- Timeout ---
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }
    }
}

