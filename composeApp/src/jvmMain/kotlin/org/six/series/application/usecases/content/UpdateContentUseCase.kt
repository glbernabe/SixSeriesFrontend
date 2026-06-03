package org.six.series.application.usecases.content

import org.six.series.model.content.Content
import org.six.series.model.content.IContentRepository

class UpdateContentUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(updatedContent: Content): Result<Unit> {
        return try {
            contentRepository.updateContent(updatedContent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}