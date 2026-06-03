package org.six.series.application.usecases.content

import org.six.series.model.content.IContentRepository

class DeleteContentUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(contentId: String): Result<Unit> {
        return try {
            val success = contentRepository.deleteContent(contentId)
            if (success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("No se encontró el contenido para eliminar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}