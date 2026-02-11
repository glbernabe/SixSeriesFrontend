package org.six.series.model

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class User @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Uuid,
    val username: String,
    val email: String,
    val image: String?,
    val status: UserStatus
)