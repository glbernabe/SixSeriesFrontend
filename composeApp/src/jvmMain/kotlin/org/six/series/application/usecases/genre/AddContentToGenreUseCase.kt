package org.six.series.application.usecases.genre

import org.six.series.model.genre.IGenreRepository

class AddContentToGenreUseCase(private val genreRepository: IGenreRepository) {
    suspend operator fun invoke(contentId: String, genreId: String): Result<Unit>{
        return try{
            genreRepository.addContentToGenre(contentId, genreId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}