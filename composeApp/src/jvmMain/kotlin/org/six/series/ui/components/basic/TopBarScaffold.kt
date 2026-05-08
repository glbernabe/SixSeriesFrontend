package org.six.series.ui.components.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.six.series.AppRoute
import org.six.series.application.usecases.LogOutUseCase
import org.six.series.model.NavigationItem
import org.six.series.ui.components.main.MainRoutes
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.ic_bullet_List
import sixseries.composeapp.generated.resources.ic_magnifying_Glass
import sixseries.composeapp.generated.resources.ic_user_Circle_Single
import java.awt.Cursor


// ALL THE ICONS FOR THE UI ON THE TOP BAR
val navItems = listOf(
    NavigationItem(null, MainRoutes.Principal, "Inicio"),
    NavigationItem(null, MainRoutes.Movies, "Películas"),
    NavigationItem(null, MainRoutes.Series, "Series"),
    NavigationItem(Res.drawable.ic_magnifying_Glass, MainRoutes.Search),
    NavigationItem(Res.drawable.ic_bullet_List, MainRoutes.Genres),
    NavigationItem(Res.drawable.ic_user_Circle_Single, MainRoutes.Profile)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveTopBar(
    navController: NavController,
    rootNavController: NavController
) {
    // --- INJECTION & STATES ---
    val logoutUseCase = koinInject<LogOutUseCase>()
    val scope = rememberCoroutineScope()
    val contentColor = MaterialTheme.colorScheme.onPrimary
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // --- TEXT STYLES ---
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
            // BRANDING
            Text(
                text = "SIX SERIES",
                style = logoStyle,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            // NAVIGATION ITEMS
            navItems.forEach { item ->
                if (item.icon == Res.drawable.ic_magnifying_Glass) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            // --- CLICK LOGIC ---
                            if (item.icon == Res.drawable.ic_user_Circle_Single) {
                                // Execution of logout through the use case and navigation to root Login
                                scope.launch {
                                    logoutUseCase.logout()
                                        .onSuccess {
                                            errorMessage = "Sesión cerrada correctamente"
                                            rootNavController.navigate(AppRoute.Login){
                                                popUpTo(0)
                                            }
                                        }
                                        .onFailure {
                                            errorMessage = "Error al intentar cerrar sesión"
                                        }
                                }
                            } else {
                                // Standard internal navigation between feature routes
                                try {
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } catch (ex: Exception) {
                                    errorMessage = "No se pudo abrir ${item.label ?: "la página"}"
                                }
                            }
                        }
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (item.icon != null) {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.label,
                                tint = contentColor,
                                modifier = Modifier
                                    .size(26.dp)
                                    .graphicsLayer {
                                        shadowElevation = 4f
                                        shape = CircleShape
                                    }
                            )
                        }

                        if (item.icon != null && !item.label.isNullOrEmpty()) {
                            Spacer(Modifier.width(8.dp))
                        }

                        if (!item.label.isNullOrEmpty()) {
                            Text(
                                text = item.label,
                                style = itemTextStyle
                            )
                        }
                    }
                }
            }
        }

        // --- ERROR OVERLAY ---
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