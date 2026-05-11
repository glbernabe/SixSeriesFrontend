package org.six.series.model.content

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
data class Content (
    val id: Long? = null,
    val title: String,
    val description: String,
    val duration: Int,
    @SerialName("age_rating")
    val ageRating: String,
    @SerialName("cover_url")
    val coverURL: String,
    @SerialName("video_url")
    val videoURL: String,
    val type: ContentType
)
