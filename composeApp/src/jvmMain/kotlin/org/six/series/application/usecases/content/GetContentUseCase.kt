package org.six.series.application.usecases.content

import org.six.series.model.content.Content
import org.six.series.model.content.IContentRepository

class GetContentUseCase(private val contentRepository: IContentRepository){
    suspend operator fun invoke(): Result<List<Content>> {
        return try {
            val content = contentRepository.getAllContent()
            if (content.isEmpty()) {
                Result.failure(Exception("No hay contenido disponible"))
            } else {
                Result.success(content)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}