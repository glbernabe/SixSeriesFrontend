package org.six.series.ui.components.basic

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.Surface
import org.koin.compose.koinInject
import org.six.series.AppRoute
import org.six.series.application.usecases.user.LogOutUseCase
import org.six.series.model.NavigationItem
import org.six.series.ui.components.main.MainRoutes
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.ic_bullet_List
import sixseries.composeapp.generated.resources.ic_magnifying_Glass
import sixseries.composeapp.generated.resources.ic_user_Circle_Single


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
    val logoutUseCase = koinInject<LogOutUseCase>()
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
                    targetValue = if (isHovered && item.icon != null) {
                        contentColor.copy(alpha = 0.12f)
                    } else {
                        Color.Transparent
                    },
                    animationSpec = tween(durationMillis = 200)
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
//                                scope.launch {
//                                    logoutUseCase.logout()
//                                        .onSuccess {
//                                            errorMessage = "Sesión cerrada correctamente"
//                                            rootNavController.navigate(AppRoute.Login) {
//                                                popUpTo(0)
//                                            }
//                                        }
//                                        .onFailure {
//                                            errorMessage = "Error al intentar cerrar sesión"
//                                        }
//                                }
                            } else {
                                try {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
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

                    if (item.icon == Res.drawable.ic_user_Circle_Single && showUserMenu) {
                        DropdownMenu(
                            expanded = showUserMenu,
                            onDismissRequest = { showUserMenu = false },
                            modifier = Modifier.background(Color.Transparent),
                            offset = DpOffset(0.dp, 8.dp)
                        ) {
                            DisplayPanelOptions()
                        }
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
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    dividerColor: Color = Color.Black.copy(alpha = 0.5f),
    dividerThickness: Dp = 2.dp,
    shape: CornerBasedShape = RoundedCornerShape(8.dp),
    elevation: Dp = 4.dp,
    leftColumnContent: @Composable ColumnScope.() -> Unit = {
        Text("Soy una opción", style = MaterialTheme.typography.bodyLarge)
    },
    rightColumnContent: @Composable ColumnScope.() -> Unit = {
        Text("Hola Mundo", style = MaterialTheme.typography.bodyLarge)
    }
) {
    Surface(
        modifier = modifier
            .width(350.dp)
            .height(500.dp),
        shape = shape,
        color = backgroundColor,
        tonalElevation = elevation
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna Izquierda
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(all = 12.dp),
                content = leftColumnContent
            )

            // Línea Divisoria Optimizada
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 16.dp), // Margen arriba/abajo para que no toque los bordes
                thickness = dividerThickness,
                color = dividerColor
            )

            // Columna Derecha
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(all = 12.dp),
                content = rightColumnContent
            )
        }
    }
}