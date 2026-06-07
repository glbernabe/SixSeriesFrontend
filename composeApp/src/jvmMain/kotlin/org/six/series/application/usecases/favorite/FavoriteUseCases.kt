package org.six.series.application.usecases.favorite

import org.six.series.model.content.Content
import org.six.series.model.favorite.IFavoriteRepository

class AddFavoriteUseCase(private val repo: IFavoriteRepository) {
    suspend operator fun invoke(contentName: String): Result<Unit> =
        repo.addFavorite(contentName)
}

class RemoveFavoriteUseCase(private val repo: IFavoriteRepository) {
    suspend operator fun invoke(contentName: String): Result<Unit> =
        repo.removeFavorite(contentName)
}

class GetMyFavoritesUseCase(private val repo: IFavoriteRepository) {
    suspend operator fun invoke(): Result<List<Content>> =
        repo.getMyFavorites()
}
