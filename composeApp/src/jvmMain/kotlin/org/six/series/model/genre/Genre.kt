package org.six.series.model.genre

import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: String? = null,
    val name: String,
)
