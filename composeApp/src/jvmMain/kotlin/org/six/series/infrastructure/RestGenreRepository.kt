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
import org.six.series.model.genre.Genre
import org.six.series.model.genre.IGenreRepository
import java.text.Normalizer
import java.util.regex.Pattern

class RestGenreRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
): IGenreRepository {

    override suspend fun getAllGenres(): List<Genre> {
        return try {
            cliente.get("$url/") {
                contentType(ContentType.Application.Json)
            }.body<List<Genre>>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getContentByGenre(genreName: String): List<Content> {
        return try {
            val normalizedName = Normalizer.normalize(genreName, Normalizer.Form.NFD)
            val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
            val cleanGenreName = pattern.matcher(normalizedName).replaceAll("").lowercase()

            cliente.get("$url/$cleanGenreName") {
                contentType(ContentType.Application.Json)
            }.body<List<Content>>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addGenre(genre: Genre) {
        try {
            cliente.post("$url/") {
                contentType(ContentType.Application.Json)
                setBody(genre)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun updateGenre(genre: Genre) {
        try {
            cliente.put("$url/") {
                contentType(ContentType.Application.Json)
                setBody(genre)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun addContentToGenre(contentId: String, genreId: String) {
        return try {
            cliente.post("$url/$genreId/content/$contentId") {
                contentType(ContentType.Application.Json)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteGenre(genreId: String): Boolean {
        return try {
            val response = cliente.delete("$url/$genreId") {
                contentType(ContentType.Application.Json)
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            throw e
        }
    }

}