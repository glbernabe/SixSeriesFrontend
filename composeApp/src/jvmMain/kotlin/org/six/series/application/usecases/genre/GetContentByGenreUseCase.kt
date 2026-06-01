package org.six.series.application.usecases.genre

import org.six.series.model.content.Content
import org.six.series.model.genre.IGenreRepository

class GetContentByGenreUseCase(private val genreRepository: IGenreRepository) {
    suspend operator fun invoke(genreName: String): Result<List<Content>> {
        return try {
            val contentList = genreRepository.getContentByGenre(genreName)
            if (contentList.isEmpty()) {
                Result.failure(Exception("No se encontró contenido para el género '$genreName'"))
            } else {
                Result.success(contentList)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}