package org.six.series.application.usecases.payment

import org.six.series.model.payment.IPaymentRepository
import org.six.series.model.payment.Payment
import org.six.series.model.payment.PaymentRequest

class GetMyPaymentsUseCase(private val repo: IPaymentRepository) {
    suspend operator fun invoke(): Result<List<Payment>> = repo.getMyPayments()
}

class MakePaymentUseCase(private val repo: IPaymentRepository) {
    suspend operator fun invoke(request: PaymentRequest): Result<Payment> =
        repo.makePayment(request)
}
