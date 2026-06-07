package org.six.series.model.history

import org.six.series.application.usecases.history.HistoryItem

interface IHistoryRepository {
    suspend fun saveHistory(profileName: String, contentTitle: String, timeViewed: Int): Result<Unit>
    suspend fun getHistory(profileName: String): Result<List<HistoryItem>>
}