package org.six.series.model.payment

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod {
    @SerialName("card") Card,
    @SerialName("paypal") PayPal
}

@Serializable
enum class PaymentStatus {
    @SerialName("completed") Completed,
    @SerialName("pending") Pending,
    @SerialName("failed") Failed
}

@Serializable
data class Payment(
    val id: String,
    @SerialName("subscription_id") val subscriptionId: String,
    @SerialName("payment_date") val paymentDate: String,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val amount: Float
)

@Serializable
data class PaymentRequest(
    @SerialName("subscription_id") val subscriptionId: String,
    val method: PaymentMethod,
    val amount: Float
)