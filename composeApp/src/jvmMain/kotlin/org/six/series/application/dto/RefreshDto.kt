package org.six.series.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshDto(
    val access_token: String,
    val id_token: String,
    val expires_in: Long,
    val token_type: String = "Bearer",
    val refresh_token: String? = null
)
