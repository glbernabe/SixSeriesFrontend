package org.six.series.model.profile

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    @SerialName("user_username") val userUsername: String,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("theme_color") val themeColor: String? = null
)

@Serializable
data class ProfileUpdateRequest(
    val name: String,
    @SerialName("theme_color") val themeColor: String? = null
)
