package org.six.series.infrastructure

import org.six.series.model.IUserRepository
import org.six.series.model.UserLogin
import org.six.series.model.UserRegister
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.*
import io.ktor.client.request.forms.submitForm
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
) : IUserRepository {

    override suspend fun loginUser(user: UserLogin) {
        try {
            val response = cliente.submitForm(
                url = "$url/login/",
                formParameters = parameters {
                    append("username", user.username)
                    append("password", user.password)
                }
            )

            // Get the body of the response
            if (response.status == HttpStatusCode.OK) {
                //TokenResponse(access_token, refresh_token)
                val tokens = response.body<Map<String, String>>()

                tokenStorage.saveTokens(
                    accessToken = tokens["access_token"] ?: "",
                    refreshToken = tokens["refresh_token"] ?: ""
                )
            } else {
                // If it's not OK then we'll throw and exception
                throw ClientRequestException(response, "Error en login")
            }

        } catch (e: ClientRequestException) {
            val text = e.response.bodyAsText()
            // Servers errors capture
            val message = try {
                val errorResponse = Json.decodeFromString<LoginDetailError>(text)
                errorResponse.detalles.joinToString { it.message }
            } catch (_: Exception) {
                "Credenciales incorrectas o error de formato"
            }
            throw IllegalArgumentException(message)
        } catch (e: Exception) {
            // Network errors
            throw IllegalStateException("No se pudo conectar con el servidor")
        }
    }

    override suspend fun signupUser(user: UserRegister) {
        cliente.post("$url/signup/") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body<Unit>()
    }


    override suspend fun logoutUser() {
        tokenStorage.clear()
    }


}