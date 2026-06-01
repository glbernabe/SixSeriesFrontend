package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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

    override suspend fun addContent(content: Content) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGenre(genreId: String) {
        TODO("Not yet implemented")
    }

}