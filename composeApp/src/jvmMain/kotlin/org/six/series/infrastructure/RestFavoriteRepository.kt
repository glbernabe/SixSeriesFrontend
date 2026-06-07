package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.six.series.model.content.Content
import org.six.series.model.favorite.IFavoriteRepository

class RestFavoriteRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
) : IFavoriteRepository {

    override suspend fun addFavorite(contentName: String): Result<Unit> {
        return try {
            cliente.post("$url/create/") {
                contentType(ContentType.Application.Json)
                url { parameters.append("content_name", contentName) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavorite(contentName: String): Result<Unit> {
        return try {
            cliente.delete("$url/") {
                contentType(ContentType.Application.Json)
                url { parameters.append("content_name", contentName) }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyFavorites(): Result<List<Content>> {
        return try {
            val result: List<Content> = cliente.get("$url/") {
                contentType(ContentType.Application.Json)
            }.body()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}