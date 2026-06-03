package org.six.series.application.usecases.content

import org.six.series.model.content.Content
import org.six.series.model.content.IContentRepository

class AddContentUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(newContent: Content): Result<Unit> {
        return try {
            contentRepository.addContent(newContent)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}