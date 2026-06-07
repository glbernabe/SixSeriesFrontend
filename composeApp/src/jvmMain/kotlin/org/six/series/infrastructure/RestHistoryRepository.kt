package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import org.six.series.application.usecases.history.HistoryItem
import org.six.series.model.history.IHistoryRepository

@Serializable
data class HistoryCreateDto(val content_title: String, val time_viewed: Int)

@Serializable
data class HistoryOutDto(val title: String)

class RestHistoryRepository(
    private val url: String,
    private val cliente: HttpClient
) : IHistoryRepository {

    override suspend fun saveHistory(profileName: String, contentTitle: String, timeViewed: Int): Result<Unit> {
        return try {
            cliente.post("$url/profiles/$profileName/history") {
                contentType(ContentType.Application.Json)
                setBody(HistoryCreateDto(contentTitle, timeViewed))
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistory(profileName: String): Result<List<HistoryItem>> {
        return try {
            val result: List<HistoryOutDto> = cliente.get("$url/$profileName") {
                contentType(ContentType.Application.Json)
            }.body()
            Result.success(result.map { HistoryItem(it.title) })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}