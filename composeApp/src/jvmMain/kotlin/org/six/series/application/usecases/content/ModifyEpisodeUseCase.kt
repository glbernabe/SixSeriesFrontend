package org.six.series.application.usecases.content

import org.six.series.model.content.Episode
import org.six.series.model.content.IContentRepository

class ModifyEpisodeUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(contentId: String, episodeId: String, updatedEpisode: Episode): Result<Unit> {
        return try {
            contentRepository.modifyEpisode(contentId, episodeId, updatedEpisode)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}