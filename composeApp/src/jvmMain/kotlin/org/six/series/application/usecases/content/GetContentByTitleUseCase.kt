package org.six.series.application.usecases.content

import org.six.series.model.content.Content
import org.six.series.model.content.IContentRepository

class GetContentByTitleUseCase(
    private val contentRepository: IContentRepository
) {
    suspend operator fun invoke(title: String): Content? {
        if (title.isBlank()) return null

        return contentRepository.getAllContent().find {
            it.title.equals(title, ignoreCase = true)
        }
    }
}