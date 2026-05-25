package org.six.series.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import org.koin.compose.viewmodel.koinViewModel
import org.six.series.model.payment.Payment
import org.six.series.model.payment.PaymentMethod
import org.six.series.model.payment.PaymentStatus
import org.six.series.model.subscription.Subscription
import org.six.series.model.subscription.SubscriptionStatus
import org.six.series.model.subscription.SubscriptionType
import org.six.series.model.subscription.subscriptionPlans
import org.six.series.profileButtonColors
import org.six.series.ui.components.viewmodels.SubscriptionUiState
import org.six.series.ui.components.viewmodels.SubscriptionViewModel

@Composable
fun SubscriptionScreen(
    viewModel: SubscriptionViewModel = koinViewModel(),
    onBack: (() -> Unit)? = null   // null cuando se usa desde el TopBar de Main
) {
    val uiState by viewModel.uiState.collectAsState()
    val showPaymentDialog by viewModel.showPaymentDialog.collectAsState()
    val pendingType by viewModel.pendingType.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val primaryColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(Unit) { viewModel.load() }

    LaunchedEffect(actionMessage) {
        actionMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.dismissMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        when (uiState) {
            is SubscriptionUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryColor)
                }
            }
            is SubscriptionUiState.Error -> {
                val msg = (uiState as SubscriptionUiState.Error).message
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(msg, color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.load() }, colors = profileButtonColors()) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            is SubscriptionUiState.Success -> {
                val data = uiState as SubscriptionUiState.Success
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(28.dp)
                ) {
                    // Cabecera con botón de volver opcional
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (onBack != null) {
                            TextButton(onClick = onBack) {
                                Text("← Volver", color = Color(0xFF888888), fontSize = 14.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(
                            "Suscripción",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )
                    }

                    // Estado actual
                    CurrentSubscriptionCard(
                        subscription = data.subscription,
                        onCancel = { viewModel.cancelSubscription() },
                        onChangePlan = {
                            // Fuerza mostrar selector de plan aunque haya sub activa
                            viewModel.requestChangePlan()
                        }
                    )

                    // Selector de plan: sin sub activa O si el usuario pide cambiar
                    val showPlanSelector = data.subscription?.status != SubscriptionStatus.Active
                            || data.showingPlanSelector
                    if (showPlanSelector) {
                        Text(
                            if (data.subscription?.status == SubscriptionStatus.Active)
                                "Cambiar de plan" else "Elige tu plan",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        subscriptionPlans.forEach { plan ->
                            PlanCard(
                                displayName = plan.displayName,
                                priceMonthly = plan.priceMonthly,
                                priceYearly = plan.priceYearly,
                                features = plan.features,
                                currentType = data.subscription?.type,
                                onSubscribeMonthly = {
                                    viewModel.requestSubscription(plan.type)
                                },
                                onSubscribeYearly = {
                                    val yearlyType = when (plan.type) {
                                        SubscriptionType.Standard -> SubscriptionType.StandardYearly
                                        SubscriptionType.Premium  -> SubscriptionType.PremiumYearly
                                        else -> plan.type
                                    }
                                    viewModel.requestSubscription(yearlyType)
                                }
                            )
                        }
                    }

                    // Historial de pagos
                    if (data.payments.isNotEmpty()) {
                        Text(
                            "Historial de pagos",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        data.payments.forEach { payment ->
                            PaymentHistoryItem(payment)
                        }
                    }
                }
            }
        }
    }

    if (showPaymentDialog && pendingType != null) {
        PaymentMethodDialog(
            subscriptionType = pendingType!!,
            onConfirm = { method -> viewModel.confirmPayment(method) },
            onDismiss = { viewModel.dismissPaymentDialog() }
        )
    }
}

@Composable
fun CurrentSubscriptionCard(
    subscription: Subscription?,
    onCancel: () -> Unit,
    onChangePlan: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                "Estado actual",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFFE6E1E5)
            )
            if (subscription == null) {
                Text("Sin suscripción activa", color = Color(0xFFCAC4D0))
            } else {
                val statusColor = when (subscription.status) {
                    SubscriptionStatus.Active  -> Color(0xFF4CAF50)
                    SubscriptionStatus.Pending -> Color(0xFFFFC107)
                    SubscriptionStatus.Expired -> MaterialTheme.colorScheme.error
                    null -> Color.Gray
                }
                val statusLabel = when (subscription.status) {
                    SubscriptionStatus.Active  -> "Activa"
                    SubscriptionStatus.Pending -> "Pendiente"
                    SubscriptionStatus.Expired -> "Expirada"
                    null -> "Desconocido"
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(statusColor, shape = RoundedCornerShape(50))
                    )
                    Text(statusLabel, color = statusColor, fontWeight = FontWeight.Bold)
                }
                val planName = when (subscription.type) {
                    SubscriptionType.Standard       -> "Estándar Mensual"
                    SubscriptionType.Premium        -> "Premium Mensual"
                    SubscriptionType.StandardYearly -> "Estándar Anual"
                    SubscriptionType.PremiumYearly  -> "Premium Anual"
                }
                Text("Plan: $planName", color = Color(0xFFE6E1E5))
                Text(
                    "Válida hasta: ${subscription.endDate}",
                    fontSize = 13.sp,
                    color = Color(0xFFCAC4D0)
                )

                if (subscription.status == SubscriptionStatus.Active) {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Cambiar plan
                        Button(
                            onClick = onChangePlan,
                            modifier = Modifier.weight(1f),
                            colors = profileButtonColors(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cambiar plan")
                        }
                        // Cancelar suscripción
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Cancelar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    displayName: String,
    priceMonthly: Float,
    priceYearly: Float,
    features: List<String>,
    currentType: SubscriptionType?,
    onSubscribeMonthly: () -> Unit,
    onSubscribeYearly: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    // Tipos que corresponden a este plan
    val monthlyType = if (displayName == "Estándar") SubscriptionType.Standard else SubscriptionType.Premium
    val yearlyType  = if (displayName == "Estándar") SubscriptionType.StandardYearly else SubscriptionType.PremiumYearly
    val isCurrentMonthly = currentType == monthlyType
    val isCurrentYearly  = currentType == yearlyType

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, primaryColor.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(displayName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = primaryColor)
            features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("✓", color = primaryColor, fontWeight = FontWeight.Bold)
                    Text(feature, fontSize = 14.sp, color = Color(0xFFE6E1E5))
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onSubscribeMonthly,
                    enabled = !isCurrentMonthly,
                    modifier = Modifier.weight(1f),
                    colors = profileButtonColors(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            if (isCurrentMonthly) "Plan actual"
                            else "${String.format("%.2f", priceMonthly)} €/mes",
                            fontWeight = FontWeight.Bold
                        )
                        if (!isCurrentMonthly) Text("Mensual", fontSize = 11.sp)
                    }
                }
                OutlinedButton(
                    onClick = onSubscribeYearly,
                    enabled = !isCurrentYearly,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryColor)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            if (isCurrentYearly) "Plan actual"
                            else "${String.format("%.2f", priceYearly)} €/año",
                            fontWeight = FontWeight.Bold
                        )
                        if (!isCurrentYearly) {
                            Text(
                                "Ahorra ${(priceMonthly * 12 - priceYearly).toInt()}€",
                                fontSize = 11.sp,
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentHistoryItem(payment: Payment) {
    val statusColor = when (payment.status) {
        PaymentStatus.Completed -> Color(0xFF4CAF50)
        PaymentStatus.Pending   -> Color(0xFFFFC107)
        PaymentStatus.Failed    -> MaterialTheme.colorScheme.error
    }
    val statusLabel = when (payment.status) {
        PaymentStatus.Completed -> "Completado"
        PaymentStatus.Pending   -> "Pendiente"
        PaymentStatus.Failed    -> "Fallido"
    }
    val methodLabel = when (payment.method) {
        PaymentMethod.Card   -> "💳 Tarjeta"
        PaymentMethod.PayPal -> "🅿 PayPal"
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(payment.paymentDate, fontWeight = FontWeight.Medium, color = Color(0xFFE6E1E5))
                Text(methodLabel, fontSize = 13.sp, color = Color(0xFFCAC4D0))
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "${String.format("%.2f", payment.amount)} €",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFFE6E1E5)
                )
                Text(statusLabel, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PaymentMethodDialog(
    subscriptionType: SubscriptionType,
    onConfirm: (PaymentMethod) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf(PaymentMethod.Card) }
    val primaryColor = MaterialTheme.colorScheme.primary

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Método de pago", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = primaryColor)
                val planLabel = when (subscriptionType) {
                    SubscriptionType.Standard       -> "Estándar Mensual — 7.99 €"
                    SubscriptionType.Premium        -> "Premium Mensual — 13.99 €"
                    SubscriptionType.StandardYearly -> "Estándar Anual — 79.99 €"
                    SubscriptionType.PremiumYearly  -> "Premium Anual — 139.99 €"
                }
                Text(planLabel, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                HorizontalDivider()
                PaymentMethodOption(
                    label = "💳 Tarjeta de crédito / débito",
                    isSelected = selectedMethod == PaymentMethod.Card,
                    onSelect = { selectedMethod = PaymentMethod.Card }
                )
                PaymentMethodOption(
                    label = "🅿 PayPal",
                    isSelected = selectedMethod == PaymentMethod.PayPal,
                    onSelect = { selectedMethod = PaymentMethod.PayPal }
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = { onConfirm(selectedMethod) },
                        modifier = Modifier.weight(1f),
                        colors = profileButtonColors()
                    ) { Text("Confirmar pago") }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodOption(label: String, isSelected: Boolean, onSelect: () -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) primaryColor else Color.Gray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                if (isSelected) primaryColor.copy(alpha = 0.07f) else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = primaryColor)
        )
        Text(label, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
    }
}