package org.six.series.application.usecases.history

import org.six.series.model.history.IHistoryRepository

class SaveHistoryUseCase(private val repo: IHistoryRepository) {
    suspend operator fun invoke(profileName: String, contentTitle: String, timeViewed: Int): Result<Unit> =
        repo.saveHistory(profileName, contentTitle, timeViewed)
}

class GetHistoryUseCase(private val repo: IHistoryRepository) {
    suspend operator fun invoke(profileName: String): Result<List<HistoryItem>> =
        repo.getHistory(profileName)
}

data class HistoryItem(val title: String)