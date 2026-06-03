package org.six.series.infrastructure

import org.six.series.model.user.IUserRepository
import org.six.series.model.user.UserLogin
import org.six.series.model.user.UserRegister
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.auth.authProviders
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.request.*
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.six.series.model.user.UserAccount
import org.six.series.model.user.UserStatusUpdate

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

        // Importante:
        cliente.authProviders
            .filterIsInstance<BearerAuthProvider>()
            .firstOrNull()
            ?.clearToken()
    }

    override suspend fun getAllUsers(): List<UserAccount> {
        return try {
            cliente.get("$url/") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateUserAccount(updatedUser: UserAccount): String {
        return try {
            val response = cliente.put("$url/${updatedUser.id}") {
                contentType(ContentType.Application.Json)
                setBody(updatedUser)
            }

            if (response.status.value in 200..299) {
                response.body<String>()
            } else {
                val errorJson = try {
                    response.body<String>()
                } catch(e: Exception) { "Error" }
                throw Exception(errorJson)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateUserStatus(userId: String, status: Boolean): Boolean {
        return try {
            val body = UserStatusUpdate(isActive = status)

            cliente.put("$url/$userId/status") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteUserById(userId: String): Boolean {
        return try {
            cliente.delete("$url/$userId") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }


}