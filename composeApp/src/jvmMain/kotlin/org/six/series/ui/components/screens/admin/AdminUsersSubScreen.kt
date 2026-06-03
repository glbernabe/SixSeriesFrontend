package org.six.series.ui.components.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.six.series.application.usecases.user.DeleteUserByIdUseCase
import org.six.series.application.usecases.user.GetAllUsersUseCase
import org.six.series.application.usecases.user.UpdateUserAccountUseCase
import org.six.series.application.usecases.user.UpdateUserStatusUseCase
import org.six.series.model.payment.PaymentStatus
import org.six.series.model.subscription.SubscriptionStatus
import org.six.series.model.subscription.SubscriptionType
import org.six.series.model.subscription.subscriptionPlans
import org.six.series.model.user.IUserRepository
import org.six.series.model.user.UserAccount


@Composable
fun AdminUsersSubScreen(
    getAllUsersUseCase: GetAllUsersUseCase,
    updateUserStatusUseCase: UpdateUserStatusUseCase,
    deleteUserByIdUseCase: DeleteUserByIdUseCase,
    updateUserAccountUseCase: UpdateUserAccountUseCase,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedUser by remember { mutableStateOf<UserAccount?>(null) }
    var usersList by remember { mutableStateOf<List<UserAccount>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    val inputFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = Color.LightGray,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color.DarkGray
    )



    LaunchedEffect(Unit) {
        isLoading = true
        getAllUsersUseCase()
            .onSuccess { users ->
                usersList = users
            }
            .onFailure {
            }
        isLoading = false
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                placeholder = { Text("Buscar por nombre o email...") },
                singleLine = true,
                colors = inputFieldColors
            )

            val filteredUsers = usersList.filter {
                it.username.contains(searchQuery, ignoreCase = true) ||
                        it.email.contains(searchQuery, ignoreCase = true)
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 240.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredUsers) { userItem ->
                    UserCard(
                        user = userItem,
                        isSelected = selectedUser?.id == userItem.id,
                        onClick = { selectedUser = userItem }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedUser != null,
            modifier = Modifier.width(460.dp).fillMaxHeight()
        ) {
            selectedUser?.let { currentUser ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF090909)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxSize().padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp).fillMaxSize()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Ficha del Usuario",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            TextButton(onClick = { selectedUser = null }) { Text("Cerrar ✕", color = Color.Gray) }
                        }

                        Spacer(Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                value = currentUser.username,
                                onValueChange = {},
                                label = { Text("Username") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                colors = inputFieldColors
                            )
                            OutlinedTextField(
                                value = currentUser.email,
                                onValueChange = {},
                                label = { Text("Email Corporativo") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                colors = inputFieldColors
                            )

                            OutlinedTextField(
                                value = if (currentUser.isActive) "ACTIVA / TRABAJANDO" else "INACTIVA / SUSPENDIDA",
                                onValueChange = {},
                                label = { Text("Estado de la cuenta (BD)") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = if (currentUser.isActive) Color(0xFF4CAF50) else Color(0xFFF44336),
                                    unfocusedTextColor = if (currentUser.isActive) Color(0xFF4CAF50) else Color(
                                        0xFFF44336
                                    ),
                                    unfocusedLabelColor = Color.LightGray,
                                    unfocusedBorderColor = Color.DarkGray
                                )
                            )

                            HorizontalDivider(color = Color.DarkGray, modifier = Modifier.padding(vertical = 4.dp))

                            Text(
                                "Estado del Plan Contratado",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )

                            if (currentUser.subscription == null) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF141414)).padding(16.dp)
                                ) {
                                    Text(
                                        "Sin suscripción activa (Cuenta Gratuita / Exenta)",
                                        color = Color.LightGray,
                                        fontSize = 13.sp
                                    )
                                }
                            } else {
                                val sub = currentUser.subscription

                                val planDisplayName = subscriptionPlans.find {
                                    it.type == sub.type ||
                                            (sub.type == SubscriptionType.StandardYearly && it.type == SubscriptionType.Standard) ||
                                            (sub.type == SubscriptionType.PremiumYearly && it.type == SubscriptionType.Premium)
                                }?.displayName ?: "Plan Personalizado"

                                val isYearly =
                                    sub.type == SubscriptionType.StandardYearly || sub.type == SubscriptionType.PremiumYearly
                                val suffix = if (isYearly) " (Anual)" else " (Mensual)"

                                val statusColor = when (sub.status) {
                                    SubscriptionStatus.Active -> Color(0xFF4CAF50)
                                    SubscriptionStatus.Expired -> Color(0xFFF44336)
                                    SubscriptionStatus.Pending -> Color(0xFFFF9800)
                                    null -> Color.Gray
                                }

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF141414)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                planDisplayName + suffix,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = (sub.status ?: SubscriptionStatus.Pending).name.uppercase(),
                                                fontWeight = FontWeight.Black,
                                                color = statusColor,
                                                fontSize = 12.sp
                                            )
                                        }
                                        Text("Fecha de inicio: ${sub.startDate}", color = Color.Gray, fontSize = 12.sp)
                                        Text(
                                            "Fecha de vencimiento: ${sub.endDate}",
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Text(
                                "Historial de Transacciones",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 14.sp
                            )

                            if (currentUser.paymentHistory.isEmpty()) {
                                Text(
                                    "No se registran transacciones para este usuario.",
                                    color = Color.DarkGray,
                                    fontSize = 13.sp
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth().weight(1f)
                                ) {
                                    items(currentUser.paymentHistory) { payment ->
                                        val isSuccess = payment.status == PaymentStatus.Completed
                                        Row(
                                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF111111)).padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    "ID Factura: #${payment.id}",
                                                    color = Color.LightGray,
                                                    fontSize = 11.sp
                                                )
                                                Text(
                                                    payment.paymentDate,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 13.sp
                                                )
                                            }
                                            Column(horizontalAlignment = Alignment.End) {
                                                Text(
                                                    "${payment.amount} €",
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    payment.status.toString().uppercase(),
                                                    color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    updateUserStatusUseCase(currentUser.id)
                                        .onSuccess { freshList ->
                                            usersList = freshList
                                            selectedUser = freshList.find { it.id == currentUser.id }
                                        }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentUser.isActive) Color(0xFFB71C1C) else Color(0xFF2E7D32)
                            ),
                            modifier = Modifier.fillMaxWidth().height(48.dp).pointerHoverIcon(PointerIcon.Hand)
                        ) {
                            Text(
                                text = if (currentUser.isActive) "Deshabilitar Cuenta (Hacer Inactiva)" else "Habilitar Cuenta (Hacer Activa)",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserCard(user: UserAccount, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(115.dp)
            .clickable { onClick() }
            .pointerHoverIcon(PointerIcon.Hand),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color(0xFF161616)
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary
        ) else null
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    user.username,
                    fontWeight = FontWeight.Bold,
                    color = if (user.isActive) Color.White else Color.Gray,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (!user.isActive) {
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFF331111))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text("INACTIVE", color = Color(0xFFF44336), fontSize = 9.sp, fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                user.email,
                color = if (user.isActive) Color.LightGray else Color.DarkGray,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "ROL: ${user.role.uppercase()}",
                color = if (user.isActive) MaterialTheme.colorScheme.primary else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        }
    }
}