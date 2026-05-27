package org.six.series.application.usecases.subscription

import org.six.series.model.subscripion.ISubscriptionRepository
import org.six.series.model.subscripion.Subscription
import org.six.series.model.subscripion.SubscriptionType

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
