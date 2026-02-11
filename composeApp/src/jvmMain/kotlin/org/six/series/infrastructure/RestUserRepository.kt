package org.six.series.infrastructure

import org.six.series.model.IUserRepository
import org.six.series.model.UserLogin
import org.six.series.model.UserRegister
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ErrorDetail(
    val field: String,
    val message: String,
)

@Serializable
data class LoginDetailError(
    val error: String,
    val detalles: List<ErrorDetail>
)

class RestUserRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
): IUserRepository {

    override suspend fun loginUser(user: UserLogin) {
        return try {
            // Intentamos deserializar la respuesta correctamente
            val tokens = cliente.post("$url/login") {
                contentType(ContentType.Application.Json)
                setBody(user)
            }.body<Map<String, String>>() // Respuesta esperada de tokens

            // Guardamos los tokens de forma segura
            tokenStorage.saveTokens(
                accessToken = tokens["access_token"] ?: "",
                refreshToken = tokens["refresh_token"] ?: ""
            )


        } catch (e: ClientRequestException) {
            // Este error se lanza cuando el servidor devuelve 4xx
            val text = e.response.bodyAsText()
            val errorResponse = try {
                Json.decodeFromString<LoginDetailError>(text)
            } catch (_: Exception) {
                null
            }

            // Lanzamos una excepción con el mensaje del servidor, si existe
            val message = errorResponse?.detalles?.joinToString { it.message } ?: text
            throw IllegalArgumentException(message)
        } catch (e: ServerResponseException) {
            // Error 5xx
            throw IllegalStateException("Error en el servidor: ${e.response.status}")
        }
    }

    override suspend fun signupUser(user: UserRegister) {
        cliente.post("$url/signup") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body<Unit>()
    }


    override suspend fun logoutUser() {
        tokenStorage.clear()
    }


}