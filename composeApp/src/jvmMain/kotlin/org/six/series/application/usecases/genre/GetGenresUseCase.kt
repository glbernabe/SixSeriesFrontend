package org.six.series.application.usecases.genre

import org.six.series.model.genre.Genre
import org.six.series.model.genre.IGenreRepository

class GetGenresUseCase(private val genreRepository: IGenreRepository) {
    suspend operator fun invoke(): Result<List<Genre>> {
        return try {
            val genres = genreRepository.getAllGenres()
            if (genres.isEmpty()) {
                Result.failure(Exception("No hay generos disponible"))
            } else {
                Result.success(genres)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}