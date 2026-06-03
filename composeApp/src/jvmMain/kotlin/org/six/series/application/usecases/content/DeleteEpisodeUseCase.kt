package org.six.series.application.usecases.content

import org.six.series.model.content.IContentRepository

class DeleteEpisodeUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(contentId: String, episodeId: String): Result<Unit> {
        return try {
            val success = contentRepository.deleteEpisode(contentId, episodeId)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se pudo eliminar el episodio. Comprueba si el ID sigue existiendo en el servidor."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}