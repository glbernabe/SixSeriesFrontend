package org.six.series.model.genre

import org.six.series.model.content.Content

interface IGenreRepository {
    suspend fun getAllGenres(): List<Genre>
    suspend fun getContentByGenre(genreName: String): List<Content>
    suspend fun addContent(content: Content)
    suspend fun deleteGenre(genreId: String)
}