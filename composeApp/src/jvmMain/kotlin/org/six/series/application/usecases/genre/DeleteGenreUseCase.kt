package org.six.series.application.usecases.genre

import org.six.series.model.genre.IGenreRepository

class DeleteGenreUseCase(private val genreRepository: IGenreRepository) {
    suspend operator fun invoke(genreId: String): Result<Unit> {
        return try {
            val success = genreRepository.deleteGenre(genreId)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se pudo eliminar el género debido a restricciones de clave foránea o ID inexistente"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}