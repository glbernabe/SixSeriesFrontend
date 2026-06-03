package org.six.series.application.usecases.genre

import org.six.series.model.genre.Genre
import org.six.series.model.genre.IGenreRepository

class AddGenreUseCase(private val genreRepository: IGenreRepository) {
    suspend operator fun invoke(newGenre: Genre): Result<Unit> {
        return try {
            genreRepository.addGenre(newGenre)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}