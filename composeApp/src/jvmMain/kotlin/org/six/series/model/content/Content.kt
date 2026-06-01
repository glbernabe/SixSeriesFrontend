package org.six.series.model.content

import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ContentType {
    @SerialName("movie")
    Movie,

    @SerialName("series")
    Series,

    @SerialName("documentary")
    Documentary
}
@Serializable
data class Content(
    val id: String? = null,
    val title: String,
    val description: String,
    val duration: LocalTime? = null,
    @SerialName("age_rating") val ageRating: String,
    @SerialName("cover_url") val coverURL: String? = null,
    @SerialName("video_url") val videoURL: String,
    val type: ContentType,
    @SerialName("logo_url") val logoURL: String? = null,
    @SerialName("portrait_url") val portraitURL: String? = null,
)
