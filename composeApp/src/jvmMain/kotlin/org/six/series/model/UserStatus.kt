package org.six.series.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserStatus {
    PENDING,
    ACTIVE,
    INACTIVE,
    SUSPENDED
}