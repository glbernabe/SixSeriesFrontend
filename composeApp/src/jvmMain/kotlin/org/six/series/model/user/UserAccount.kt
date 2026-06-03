package org.six.series.model.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.six.series.model.payment.Payment
import org.six.series.model.subscription.Subscription
@Serializable
data class UserAccount(
    val id: String,
    val username: String,
    val email: String,
    @SerialName("rol") val role: String,
    @SerialName("status") val isActive: Boolean,
    val subscription: Subscription? = null,
    @SerialName("payment_history") val paymentHistory: List<Payment> = emptyList()
)
@Serializable
data class UserStatusUpdate(
    @SerialName("is_active") val isActive: Boolean
)