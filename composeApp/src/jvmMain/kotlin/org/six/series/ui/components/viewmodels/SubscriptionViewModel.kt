package org.six.series.ui.components.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.six.series.application.usecases.payment.GetMyPaymentsUseCase
import org.six.series.application.usecases.payment.MakePaymentUseCase
import org.six.series.application.usecases.subscription.CancelSubscriptionUseCase
import org.six.series.application.usecases.subscription.CreateSubscriptionUseCase
import org.six.series.application.usecases.subscription.GetMySubscriptionUseCase
import org.six.series.model.payment.Payment
import org.six.series.model.payment.PaymentMethod
import org.six.series.model.payment.PaymentRequest
import org.six.series.model.subscription.Subscription
import org.six.series.model.subscription.SubscriptionStatus
import org.six.series.model.subscription.SubscriptionType

sealed class SubscriptionUiState {
    object Loading : SubscriptionUiState()
    data class Success(
        val subscription: Subscription?,
        val payments: List<Payment>,
        val showingPlanSelector: Boolean = true
    ) : SubscriptionUiState()
    data class Error(val message: String) : SubscriptionUiState()
}

class SubscriptionViewModel(
    private val getMySubscriptionUseCase: GetMySubscriptionUseCase,
    private val createSubscriptionUseCase: CreateSubscriptionUseCase,
    private val cancelSubscriptionUseCase: CancelSubscriptionUseCase,
    private val getMyPaymentsUseCase: GetMyPaymentsUseCase,
    private val makePaymentUseCase: MakePaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SubscriptionUiState>(
        SubscriptionUiState.Success(null, emptyList())
    )
    val uiState: StateFlow<SubscriptionUiState> = _uiState.asStateFlow()

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    private val _showPaymentDialog = MutableStateFlow(false)
    val showPaymentDialog: StateFlow<Boolean> = _showPaymentDialog.asStateFlow()

    private val _pendingType = MutableStateFlow<SubscriptionType?>(null)
    val pendingType: StateFlow<SubscriptionType?> = _pendingType.asStateFlow()

    private val _paymentError = MutableStateFlow<String?>(null)
    val paymentError: StateFlow<String?> = _paymentError.asStateFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.value = SubscriptionUiState.Loading
            val subResult = getMySubscriptionUseCase()
            val payResult = getMyPaymentsUseCase()
            if (subResult.isSuccess && payResult.isSuccess) {
                _uiState.value = SubscriptionUiState.Success(
                    subscription = subResult.getOrNull(),
                    payments = payResult.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = SubscriptionUiState.Error("Error al cargar los datos")
            }
        }
    }

    fun requestChangePlan() {
        val current = _uiState.value as? SubscriptionUiState.Success ?: return
        _uiState.value = current.copy(showingPlanSelector = true)
    }

    fun requestSubscription(type: SubscriptionType) {
        _pendingType.value = type
        _showPaymentDialog.value = true
        _paymentError.value = null
    }

    fun dismissPaymentDialog() {
        _showPaymentDialog.value = false
        _pendingType.value = null
        _paymentError.value = null
    }

    fun confirmPayment(method: PaymentMethod) {
        val type = _pendingType.value ?: return
        _paymentError.value = null

        viewModelScope.launch {
            createSubscriptionUseCase(type)
                .onSuccess { sub ->
                    val amount = when (type) {
                        SubscriptionType.Standard       -> 7.99f
                        SubscriptionType.Premium        -> 13.99f
                        SubscriptionType.StandardYearly -> 79.99f
                        SubscriptionType.PremiumYearly  -> 139.99f
                    }
                    makePaymentUseCase(PaymentRequest(sub.id, method, amount))
                        .onSuccess {
                            _actionMessage.value = "¡Suscripción activada correctamente!"
                            _showPaymentDialog.value = false // Solo cerramos el diálogo si el pago es exitoso
                            _pendingType.value = null
                            load()
                        }
                        .onFailure { exception ->
                            _paymentError.value = exception.message ?: "Pago fallido. Inténtalo de nuevo."
                        }
                }
                .onFailure { exception ->
                    _paymentError.value = exception.message ?: "Error al crear la suscripción"
                }
        }
    }

    fun cancelPendingSubscription() {
        val state = _uiState.value as? SubscriptionUiState.Success ?: return
        val currentSub = state.subscription ?: return

        if (currentSub.status != SubscriptionStatus.Pending) return

        dismissPaymentDialog()

        viewModelScope.launch {
            cancelSubscriptionUseCase(currentSub.id)
                .onSuccess {
                    _actionMessage.value = "Suscripción pendiente cancelada con éxito."
                    load() // Refresca la lista de planes en segundo plano
                }
                .onFailure { exception ->
                    _actionMessage.value = exception.message ?: "Error al descartar la suscripción pendiente."
                    load() // Recargamos toda la UI
                }
        }
    }

    fun cancelSubscription() {
        val state = _uiState.value as? SubscriptionUiState.Success ?: return
        val subId = state.subscription?.id ?: return
        viewModelScope.launch {
            cancelSubscriptionUseCase(subId)
                .onSuccess {
                    _actionMessage.value = "Suscripción cancelada"
                    load()
                }
                .onFailure {
                    _actionMessage.value = "Error al cancelar la suscripción"
                }
        }
    }

    fun dismissMessage() { _actionMessage.value = null }
}