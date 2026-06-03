package org.six.series.model.content

interface IContentRepository {
    suspend fun getAllContent(): List<Content>
    suspend fun findByTitle(title: String): Content?
    suspend fun getEpisodes(contentId: String): List<Episode>
    suspend fun addContent(content: Content)
    suspend fun updateContent(content: Content)
    suspend fun deleteContent(contentId: String): Boolean
    suspend fun addEpisode(contentId: String, episode: Episode)
    suspend fun deleteEpisode(contentId: String, episodeId: String): Boolean
    suspend fun modifyEpisode(contentId: String, episodeId: String, updatedEpisode: Episode)

}