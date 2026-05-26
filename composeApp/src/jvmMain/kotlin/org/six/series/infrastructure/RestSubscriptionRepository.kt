package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.six.series.model.subscription.ISubscriptionRepository
import org.six.series.model.subscription.Subscription
import org.six.series.model.subscription.SubscriptionType

class RestSubscriptionRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
) : ISubscriptionRepository {

    override suspend fun getMySubscription(): Result<Subscription?> {
        return try {
            val response = cliente.get("$url/me/") {
                contentType(ContentType.Application.Json)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val sub = response.body<Subscription>()
                    Result.success(sub)
                }
                HttpStatusCode.NotFound -> {
                    Result.success(null)
                }
                else -> {
                    Result.failure(Exception("Error de servidor: Código de error ${response.status.value}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createSubscription(type: SubscriptionType): Result<Subscription> {
        return try {
            val response = cliente.post("$url/") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("type" to type.toApiString()))
            }
            if (response.status.isSuccess()) {
                Result.success(response.body<Subscription>())
            } else {
                Result.failure(Exception("Fallo en crear una subscripción: Código de error ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelSubscription(id: String): Result<Unit> {
        return try {
            val response = cliente.delete("$url/me/") {
                contentType(ContentType.Application.Json)
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Fallo en cancelar una subscripción: Código de error ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}