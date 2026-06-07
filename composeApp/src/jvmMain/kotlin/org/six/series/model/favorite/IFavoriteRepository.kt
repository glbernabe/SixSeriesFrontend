package org.six.series.model.favorite

import org.six.series.model.content.Content

interface IFavoriteRepository {
    suspend fun addFavorite(contentName: String): Result<Unit>
    suspend fun removeFavorite(contentName: String): Result<Unit>
    suspend fun getMyFavorites(): Result<List<Content>>
}
 