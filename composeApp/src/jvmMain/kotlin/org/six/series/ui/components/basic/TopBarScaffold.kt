package org.six.series.ui.components.basic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.six.series.AppRoute
import org.six.series.application.usecases.user.LogOutUseCase
import org.six.series.infrastructure.TokenStorage
import org.six.series.infrastructure.UserTokenData
import org.six.series.model.NavigationItem
import org.six.series.ui.components.main.MainRoutes
import org.six.series.ui.components.viewmodels.ProfileViewModel
import org.six.series.ui.components.viewmodels.SubscriptionViewModel
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.ic_Grid
import sixseries.composeapp.generated.resources.ic_magnifying_Glass
import sixseries.composeapp.generated.resources.ic_user_Circle_Single


val navItems = listOf(
    NavigationItem(null, MainRoutes.Principal, "Inicio"),
    NavigationItem(null, MainRoutes.Movies, "Películas"),
    NavigationItem(null, MainRoutes.Series, "Series"),
    NavigationItem(Res.drawable.ic_magnifying_Glass, MainRoutes.Search),
    NavigationItem(Res.drawable.ic_Grid, MainRoutes.Genres),
    NavigationItem(Res.drawable.ic_user_Circle_Single, MainRoutes.Profile)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveTopBar(
    navController: NavController,
    rootNavController: NavController
) {
    val tokenStorage = koinInject<TokenStorage>()
    val logoutUseCase = koinInject<LogOutUseCase>()
    val profileViewModel = koinInject<ProfileViewModel>()
    val subscriptionViewModel = koinInject<SubscriptionViewModel>()

    val scope = rememberCoroutineScope()
    val contentColor = MaterialTheme.colorScheme.onPrimary
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showUserMenu by remember { mutableStateOf(false) }

    val logoStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 6.sp,
        color = contentColor,
        shadow = Shadow(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
            offset = Offset(0f, 0f),
            blurRadius = 12f
        )
    )

    val itemTextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = contentColor,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(2f, 4f),
            blurRadius = 8f
        )
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, Color.Transparent)
                    )
                )
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SIX SERIES",
                style = logoStyle,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            navItems.forEach { item ->
                if (item.icon == Res.drawable.ic_magnifying_Glass) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                val interactionSource = remember { MutableInteractionSource() }
                val isHovered by interactionSource.collectIsHoveredAsState()

                val animatedBackgroundColor by animateColorAsState(
                    targetValue = if (isHovered && item.icon != null) contentColor.copy(alpha = 0.12f)
                    else Color.Transparent,
                    animationSpec = tween(200)
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (item.icon == Res.drawable.ic_user_Circle_Single) {
                                showUserMenu = !showUserMenu
                            } else {
                                try {
                                    item.route?.let {
                                        navController.navigate(it) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = false
                                            }
                                            launchSingleTop = true
                                            restoreState = false
                                        }
                                    }
                                } catch (ex: Exception) {
                                    errorMessage = "No se pudo abrir ${item.label ?: "la página"}"
                                }
                            }
                        }
                        .pointerHoverIcon(PointerIcon.Hand)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (item.icon != null) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color = animatedBackgroundColor, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(item.icon),
                                    contentDescription = item.label,
                                    tint = contentColor,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        if (item.label != null && item.icon == null) {
                            Text(text = item.label, style = itemTextStyle)
                        }
                    }
                }
            }


            val userData by tokenStorage.userDataFlow.collectAsState()
            if (showUserMenu) {
                Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopEnd)) {
                    DropdownMenu(
                        expanded = showUserMenu,
                        onDismissRequest = { showUserMenu = false },
                        offset = DpOffset(x = (-8).dp, y = 4.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp),
                        tonalElevation = 0.dp,
                        shadowElevation = 8.dp,
                        modifier = Modifier.background(Color.Transparent)
                    ) {
                        DisplayPanelOptions(
                            userData = userData,
                            backgroundColor = Color.Transparent,
                            elevation = 0.dp,
                            onChangeProfileClick = {
                                showUserMenu = false
                                rootNavController.navigate(AppRoute.ProfileSelector) {
                                    popUpTo(AppRoute.Main) { inclusive = true }
                                }
                            },
                            onAccountSettingsClick = {
                                showUserMenu = false
                                navController.navigate(MainRoutes.Profile) { popUpTo(0) }
                            },
                            // ── NUEVO: navega a Favoritos / Seguir viendo ──
                            onMyListsClick = {
                                showUserMenu = false
                                navController.navigate(MainRoutes.Favorites) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            onSignOutClick = {
                                showUserMenu = false
                                scope.launch {
                                    logoutUseCase.logout()
                                        .onSuccess {
                                            errorMessage = "Sesión cerrada correctamente"
                                            profileViewModel.clearState()
                                            subscriptionViewModel.clearState()
                                            rootNavController.navigate(AppRoute.Login) {
                                                popUpTo(0) { inclusive = true }
                                            }
                                        }
                                        .onFailure {
                                            errorMessage = "Error al intentar cerrar sesión"
                                        }
                                }
                            }
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentAlignment = Alignment.TopCenter
            ) {
                ErrorNotification(
                    message = errorMessage!!,
                    onDismiss = { errorMessage = null }
                )
            }
        }
    }
}

@Composable
fun DisplayPanelOptions(
    userData: UserTokenData? = null,
    backgroundColor: Color = Color.Transparent,
    elevation: Dp = 0.dp,
    onChangeProfileClick: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    onMyListsClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .width(350.dp)
            .height(500.dp),
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor,
        shadowElevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.98f)),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(all = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Manage",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                StreamingOptionItem(icon = Icons.Default.AccountCircle, label = "Perfiles") { onChangeProfileClick() }
                StreamingOptionItem(icon = Icons.Default.Favorite, label = "Mis Listas") { onMyListsClick() }
                StreamingOptionItem(icon = Icons.Default.Settings, label = "Ajustes") { onAccountSettingsClick() }
                StreamingOptionItem(icon = Icons.Default.ExitToApp, label = "Cerrar Sesión") { onSignOutClick() }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .border(width = 3.dp, color = Color.Black.copy(alpha = 0.5f))
            ) { }

            // Columna derecha: info usuario
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(all = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = userData?.username ?: "Guest",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = when (userData?.role) {
                        "superuser" -> "Administrador"
                        "user" -> "Usuario"
                        null -> "No Role"
                        else -> userData.role
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun StreamingOptionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isHovered) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent,
        label = "HoverAnimation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .hoverable(interactionSource = interactionSource)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}