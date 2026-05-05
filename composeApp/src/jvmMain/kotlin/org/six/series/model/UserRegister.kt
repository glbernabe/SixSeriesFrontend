package org.six.series.model

import kotlinx.serialization.Serializable
@Serializable
data class UserRegister(
    val username: String,
    val password: String,
    val email: String
)
