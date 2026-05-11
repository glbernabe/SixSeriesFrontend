package org.six.series.model.content

interface IContentRepository {
    suspend fun getAllContent(): List<Content>
    suspend fun findByTitle(title: String): Content?
}