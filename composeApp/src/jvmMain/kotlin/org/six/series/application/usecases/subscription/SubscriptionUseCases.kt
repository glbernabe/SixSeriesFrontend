package org.six.series.application.usecases.subscription

import org.six.series.model.subscription.ISubscriptionRepository
import org.six.series.model.subscription.Subscription
import org.six.series.model.subscription.SubscriptionType

class GetMySubscriptionUseCase(private val repo: ISubscriptionRepository) {
    suspend operator fun invoke(): Result<Subscription?> = repo.getMySubscription()
}

class CreateSubscriptionUseCase(private val repo: ISubscriptionRepository) {
    suspend operator fun invoke(type: SubscriptionType): Result<Subscription> =
        repo.createSubscription(type)
}

class CancelSubscriptionUseCase(private val repo: ISubscriptionRepository) {
    suspend operator fun invoke(id: String): Result<Unit> = repo.cancelSubscription(id)
}
