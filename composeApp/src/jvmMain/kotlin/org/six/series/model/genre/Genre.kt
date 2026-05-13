package org.six.series.model.genre

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Long,
    val name: String,
)
