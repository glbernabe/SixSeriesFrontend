package org.six.series.model.subscription

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SubscriptionType {
    @SerialName("standard") Standard,
    @SerialName("premium") Premium,
    @SerialName("standard_yearly") StandardYearly,
    @SerialName("premium_yearly") PremiumYearly,
    @SerialName("admin_life") AdminLife;

    fun toApiString(): String = when(this) {
        Standard -> "standard"
        Premium -> "premium"
        StandardYearly -> "standard_yearly"
        PremiumYearly -> "premium_yearly"
        AdminLife -> "admin_life"
    }
}

@Serializable
enum class SubscriptionStatus {
    @SerialName("pending") Pending,
    @SerialName("active") Active,
    @SerialName("expired") Expired
}

@Serializable
data class Subscription(
    val id: String,
    @SerialName("user_username") val userUsername: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    val status: SubscriptionStatus? = null,
    val type: SubscriptionType
)

data class SubscriptionPlan(
    val type: SubscriptionType,
    val displayName: String,
    val priceMonthly: Float,
    val priceYearly: Float,
    val features: List<String>
)

val subscriptionPlans = listOf(
    SubscriptionPlan(
        type = SubscriptionType.Standard,
        displayName = "Estándar",
        priceMonthly = 7.99f,
        priceYearly = 79.99f,
        features = listOf("HD 1080p", "1 pantalla simultánea", "Sin anuncios")
    ),
    SubscriptionPlan(
        type = SubscriptionType.Premium,
        displayName = "Premium",
        priceMonthly = 13.99f,
        priceYearly = 139.99f,
        features = listOf("4K Ultra HD", "4 pantallas simultáneas", "Sin anuncios", "Descargas offline", "Audio Dolby Atmos")
    )
)