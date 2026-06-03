package org.six.series.model.genre

import org.six.series.model.content.Content

interface IGenreRepository {
    suspend fun getAllGenres(): List<Genre>
    suspend fun getContentByGenre(genreName: String): List<Content>
    suspend fun addGenre(genre: Genre)
    suspend fun updateGenre(genre: Genre)
    suspend fun addContentToGenre(contentId: String, genreId: String)
    suspend fun deleteGenre(genreId: String): Boolean
}