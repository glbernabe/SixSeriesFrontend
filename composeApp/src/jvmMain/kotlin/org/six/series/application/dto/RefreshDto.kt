package org.six.series.application.dto

import kotlinx.serialization.Serializable

@Serializable
data class RefreshDto(
    val access_token: String,
    val id_token: String,    // Aquí va el JWT con los claims del perfil
    val expires_in: Long,    // Segundos (ej. 3600)
    val token_type: String = "Bearer",
    val refresh_token: String? = null
)
