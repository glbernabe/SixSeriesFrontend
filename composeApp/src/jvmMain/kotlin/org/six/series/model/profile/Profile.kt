package org.six.series.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("user_username") val userUsername: String,
    val name: String,
    @SerialName("profile_color") val profileColor: String? = null // Cambiado de theme_color a profile_color
)

@Serializable
data class ProfileCreateRequest(
    val name: String,
    val color: String
)

@Serializable
data class ProfileUpdateRequest(
    val name: String,
    @SerialName("profile_color") val profileColor: String? = null
)