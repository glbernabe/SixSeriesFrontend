package org.six.series.ui.components.screens

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.six.series.application.usecases.profile.CreateProfileUseCase
import org.six.series.application.usecases.profile.GetMyProfilesUseCase
import org.six.series.model.profile.Profile
import org.six.series.profileButtonColors
import org.six.series.ui.components.viewmodels.ProfileUiState
import org.six.series.ui.components.viewmodels.ProfileViewModel

@Composable
fun ProfileSelectorScreen(
    viewModel: ProfileViewModel,
    onProfileSelected: (Profile) -> Unit,
    onManageSubscription: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSubscriptionAlert by viewModel.showSubscriptionAlert.collectAsStateWithLifecycle()
    var showCreateDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D)),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            is ProfileUiState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Error: ${state.message}", color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.loadProfile() }, colors = profileButtonColors()) {
                        Text("Reintentar")
                    }
                }
            }

            else -> {
                val profiles = if (state is ProfileUiState.Success) listOf(state.profile) else emptyList()
                val totalItems = if (profiles.size < 5) profiles.size + 1 else profiles.size

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 48.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    // ── Header ────────────────────────────────────────────────
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(top = 90.dp)
                    ) {
                        Text(
                            "SIX SERIES",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 6.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "¿Quién está viendo?",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE6E1E5)
                        )
                    }

                    Spacer(Modifier.height(100.dp))

                    // ──────────────────────── Profile grid ────────────────────────
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(count = maxOf(1, totalItems)),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                        modifier = Modifier
                            .width(1000.dp)
                            .height(190.dp)
                    ) {
                        items(profiles) { profile ->
                            ProfileCard(
                                profile = profile,
                                onClick = { onProfileSelected(profile) }
                            )
                        }
                        if (profiles.size < 5) {
                            item {
                                AddProfileCard(onClick = { showCreateDialog = true })
                            }
                        }
                    }

                    // ──────────────────────── Subscription link ────────────────────────
                    TextButton(
                        onClick = onManageSubscription,
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
                    ) {
                        Text(
                            "Gestionar suscripción →",
                            color = Color(0xFF888888),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateProfileDialog(
            onConfirm = { name ->
                showCreateDialog = false
                viewModel.createProfile(name)
            },
            onDismiss = { showCreateDialog = false }
        )
    }

    if (showSubscriptionAlert) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissSubscriptionAlert() },
            containerColor = Color(0xFF1A1A1A),
            title = { Text("Suscripción requerida", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
            text = { Text("Debes de suscribirte a uno de nuestros planes para poder acceder y ver el contenido.", color = Color(0xFFE6E1E5)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissSubscriptionAlert()
                        onManageSubscription()
                    },
                    colors = profileButtonColors()
                ) { Text("Ver planes") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissSubscriptionAlert() }) { Text("Cancelar", color = Color(0xFF888888)) }
            }
        )
    }
}

// ──────────────────────── ProfileCard  ────────────────────────

@Composable
private fun ProfileCard(profile: Profile, onClick: () -> Unit) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val avatarColor = remember(profile.profileColor) {
        profile.profileColor?.let { hex ->
            try {
                val colorLong = hex.removePrefix("#").toLong(16) or 0xFF000000L
                Color(colorLong)
            } catch (e: Exception) { null }
        }
    } ?: primaryColor

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(avatarColor)
                .border(3.dp, avatarColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profile.name.take(1).uppercase(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = avatarColor.contrastingTextColor()
            )
        }
        Text(
            text = profile.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE6E1E5),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Composable
private fun AddProfileCard(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color(0xFF2A2A2A))
                .border(2.dp, Color(0xFF444444), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("+", fontSize = 40.sp, color = Color(0xFF888888), fontWeight = FontWeight.Light)
        }
        Text(
            text = "Añadir perfil",
            fontSize = 15.sp,
            color = Color(0xFF888888),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CreateProfileDialog(onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    val primaryColor = MaterialTheme.colorScheme.primary
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A1A),
        title = { Text("Nuevo perfil", color = primaryColor, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del perfil") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    focusedLabelColor = primaryColor,
                    unfocusedBorderColor = Color(0xFF444444),
                    unfocusedLabelColor = Color(0xFF888888),
                    focusedTextColor = Color(0xFFE6E1E5),
                    unfocusedTextColor = Color(0xFFE6E1E5)
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onConfirm(name.trim()) },
                colors = profileButtonColors(),
                enabled = name.isNotBlank()
            ) { Text("Crear") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = Color(0xFF888888)) }
        }
    )
}

private fun Color.contrastingTextColor(): Color {
    val luminance = 0.2126f * red + 0.7152f * green + 0.0722f * blue
    return if (luminance > 0.5f) Color.Black else Color.White
}