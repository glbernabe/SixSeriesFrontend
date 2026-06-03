package org.six.series.application.usecases.content

import org.six.series.model.content.Episode
import org.six.series.model.content.IContentRepository

class AddEpisodeUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(contentId: String, episode: Episode): Result<Unit> {
        return try {
            contentRepository.addEpisode(contentId, episode)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}