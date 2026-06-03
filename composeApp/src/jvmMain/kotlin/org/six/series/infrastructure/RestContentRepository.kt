package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
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
    override suspend fun addContent(content: Content) {
        try {
            cliente.post("$url/") {
                contentType(ContentType.Application.Json)
                setBody(content)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateContent(content: Content) {
        try {
            cliente.put("$url/") {
                contentType(ContentType.Application.Json)
                setBody(content)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteContent(contentId: String): Boolean {
        return try {
            cliente.delete("$url/$contentId") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun addEpisode(contentId: String, episode: Episode) {
        try {
            cliente.post("$url/$contentId/episodes/") {
                contentType(ContentType.Application.Json)
                setBody(episode)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteEpisode(contentId: String, episodeId: String): Boolean {
        return try {
            cliente.delete("$url/$contentId/episodes/$episodeId") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun modifyEpisode(contentId: String, episodeId: String, updatedEpisode: Episode) {
        try {
            cliente.put("$url/$contentId/episodes/$episodeId") {
                contentType(ContentType.Application.Json)
                setBody(updatedEpisode)
            }
        } catch (e: Exception) {
            throw e
        }
    }
}