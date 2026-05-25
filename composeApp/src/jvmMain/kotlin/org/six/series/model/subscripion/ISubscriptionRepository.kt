package org.six.series.model.subscription

interface ISubscriptionRepository {
    suspend fun getMySubscription(): Result<Subscription?>
    suspend fun createSubscription(type: SubscriptionType): Result<Subscription>
    suspend fun cancelSubscription(id: String): Result<Unit>
}
