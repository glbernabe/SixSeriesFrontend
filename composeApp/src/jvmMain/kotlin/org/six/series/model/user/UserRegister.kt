package org.six.series.model.user

import kotlinx.serialization.Serializable
@Serializable
data class UserRegister(
    val username: String,
    val password: String,
    val email: String,
    val rol: String? = null,
    val permissions: String? = null,
)
