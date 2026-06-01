package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.six.series.model.content.Content
import org.six.series.model.content.Episode
import org.six.series.model.content.IContentRepository

class RestContentRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
) : IContentRepository {

    override suspend fun getAllContent(): List<Content> {
        return try {
            cliente.get("$url/") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun findByTitle(title: String): Content? {
        return try {
            cliente.get("$url/$title") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getEpisodes(contentId: String): List<Episode> {
        return try {
            cliente.get("$url/$contentId/episodes") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            emptyList()
        }
    }
}