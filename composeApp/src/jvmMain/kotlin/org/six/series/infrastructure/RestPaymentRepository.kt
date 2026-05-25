package org.six.series.infrastructure

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.six.series.model.payment.IPaymentRepository
import org.six.series.model.payment.Payment
import org.six.series.model.payment.PaymentRequest

class RestPaymentRepository(
    private val url: String,
    private val cliente: HttpClient,
    private val tokenStorage: TokenStorage
) : IPaymentRepository {

    override suspend fun getMyPayments(): Result<List<Payment>> {
        return try {
            val payments = cliente.get("$url/me/") {
                contentType(ContentType.Application.Json)
            }.body<List<Payment>>()
            Result.success(payments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun makePayment(request: PaymentRequest): Result<Payment> {
        return try {
            val payment = cliente.post("$url/add/") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body<Payment>()
            Result.success(payment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
