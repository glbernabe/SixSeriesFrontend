package org.six.series.application.usecases.content

import org.six.series.model.content.Episode
import org.six.series.model.content.IContentRepository

class GetEpisodesUseCase(private val contentRepository: IContentRepository) {
    suspend operator fun invoke(contentId: String): Result<List<Episode>> {
        return try {
            val episodes = contentRepository.getEpisodes(contentId)
            Result.success(episodes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}