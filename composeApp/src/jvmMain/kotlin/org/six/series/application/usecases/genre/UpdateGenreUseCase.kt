package org.six.series.application.usecases.genre

import org.six.series.model.genre.Genre
import org.six.series.model.genre.IGenreRepository

class UpdateGenreUseCase(private val genreRepository: IGenreRepository) {
    suspend operator fun invoke(genre: Genre): Result<Unit>{
        return try{
            genreRepository.updateGenre(genre)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}