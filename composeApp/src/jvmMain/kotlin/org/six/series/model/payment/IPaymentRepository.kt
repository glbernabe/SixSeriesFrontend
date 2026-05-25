package org.six.series.model.payment

interface IPaymentRepository {
    suspend fun getMyPayments(): Result<List<Payment>>
    suspend fun makePayment(request: PaymentRequest): Result<Payment>
}