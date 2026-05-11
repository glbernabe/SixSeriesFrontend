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
data class Content (
    val id: Long? = null,
    val title: String,
    val description: String,
    val duration: LocalTime? = null, //HH:MM:SS | HH:MM
    @SerialName("age_rating")
    val ageRating: String,
    @SerialName("cover_url")
    val coverURL: String,
    @SerialName("video_url")
    val videoURL: String,
    val type: ContentType
)

/*
INSERT INTO CONTENT (
    id,
    title,
    description,
    duration,
    ageRating,
    coverUrl,
    videoUrl,
    type
) VALUES
(   '123',
    'The Batman',
    'En su segundo año luchando contra el crimen, Batman explora la corrupción en Gotham City.',
    '02:56:00', -- Casi 3 horas de duración
    '16',
    'https://m.media-amazon.com/images/M/MV5BZjJiYTliODMtNjM3MS00MzkxLWFlZGUtNmRmYWI1MzFlZmRiXkEyXkFqcGc@._V1_.jpg',
    'https://streamimdb.ru/embed/movie/tt1877830',
    'movie'
),
(   '333',
    'Joker',
    'La pasión de Arthur Fleck por hacer reír se convierte en una caída en el abismo de la locura.',
    '02:02:00',
    '18',
    'https://blog.normacomics.com/wp-content/uploads/2021/10/Facepaint.jpeg',
    'https://streamimdb.ru/embed/movie/tt7286456',
    'movie'
);
 */