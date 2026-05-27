package org.six.series.model.content

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Episode(
    val id: String,
    @SerialName("content_id")  val contentId: String,
    val season: Int = 1,
    val episode: Int,
    val title: String,
    val description: String? = null,
    val duration: LocalTime? = null,
    @SerialName("video_url")   val videoUrl: String,
    @SerialName("cover_url")   val coverUrl: String? = null,
)