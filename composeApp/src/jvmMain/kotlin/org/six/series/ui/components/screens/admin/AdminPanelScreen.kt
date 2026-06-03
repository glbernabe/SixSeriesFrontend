package org.six.series.ui.components.screens.admin


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.six.series.AdminRoute
import org.six.series.application.usecases.content.AddContentUseCase
import org.six.series.application.usecases.content.DeleteContentUseCase
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.application.usecases.content.UpdateContentUseCase
import org.six.series.application.usecases.genre.AddGenreUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.genre.UpdateGenreUseCase
import org.six.series.application.usecases.genre.DeleteGenreUseCase
import org.six.series.application.usecases.user.DeleteUserByIdUseCase
import org.six.series.application.usecases.user.GetAllUsersUseCase
import org.six.series.application.usecases.user.UpdateUserAccountUseCase
import org.six.series.application.usecases.user.UpdateUserStatusUseCase
import org.six.series.model.NavigationItem
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.ic_Grid
import sixseries.composeapp.generated.resources.ic_button_Play
import sixseries.composeapp.generated.resources.ic_user_Circle_Single

@Composable
fun AdminPanelScreen(
    onBackToProfiles: () -> Unit,
    getAllContentUseCase: GetContentUseCase = koinInject(),
    insertContentUseCase: AddContentUseCase = koinInject(),
    updateContentUseCase: UpdateContentUseCase = koinInject(),
    deleteContentUseCase: DeleteContentUseCase = koinInject(),
    getGenresUseCase: GetGenresUseCase = koinInject(),
    addGenreUseCase: AddGenreUseCase = koinInject(),
    updateGenreUseCase: UpdateGenreUseCase = koinInject(),
    deleteGenreUseCase: DeleteGenreUseCase = koinInject(),
    getAllUsersUseCase: GetAllUsersUseCase = koinInject(),
    updateUserStatusUseCase: UpdateUserStatusUseCase = koinInject(),
    deleteUserByIdUseCase: DeleteUserByIdUseCase = koinInject(),
    updateUserAccountUseCase: UpdateUserAccountUseCase = koinInject(),

    ) {
    val adminNavController = rememberNavController()

    val navBackStackEntry by adminNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: AdminRoute.Users

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        // ── BARRA LATERAL ───────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(260.dp)
                .background(Color(0xFF090909))
                .padding(vertical = 24.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "SIX MANAGEMENT",
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 40.dp, start = 8.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SidebarItem(
                    item = NavigationItem(Res.drawable.ic_user_Circle_Single, null, "Usuarios" ),
                    isSelected = currentRoute == AdminRoute.Users,
                    onClick = {
                        if (currentRoute != AdminRoute.Users) {
                            adminNavController.navigate(AdminRoute.Users) {
                                popUpTo(AdminRoute.Users) { inclusive = true }
                            }
                        }
                    }
                )
                SidebarItem(
                    item = NavigationItem(Res.drawable.ic_button_Play, null, "Contenido"),
                    isSelected = currentRoute == AdminRoute.Contents,
                    onClick = {
                        if (currentRoute != AdminRoute.Contents) {
                            adminNavController.navigate(AdminRoute.Contents)
                        }
                    }
                )
                SidebarItem(
                    item = NavigationItem(Res.drawable.ic_Grid, null, "Generos"),
                    isSelected = currentRoute == AdminRoute.Genres,
                    onClick = {
                        if (currentRoute != AdminRoute.Genres) {
                            adminNavController.navigate(AdminRoute.Genres)
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onBackToProfiles() }
                    .pointerHoverIcon(PointerIcon.Hand)
                    .padding(12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "↩ Volver a Perfiles",
                    color = Color(0xFF888888),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ── CONTENEDOR PRINCIPAL INTERNO ──
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .background(Color(0xFF141414))
                .padding(40.dp)
        ) {
            NavHost(
                navController = adminNavController,
                startDestination = AdminRoute.Users
            ) {
                composable(AdminRoute.Users) {
                    AdminUsersSubScreen(
                        getAllUsersUseCase = getAllUsersUseCase,
                        updateUserStatusUseCase = updateUserStatusUseCase,
                        deleteUserByIdUseCase = deleteUserByIdUseCase,
                        updateUserAccountUseCase = updateUserAccountUseCase
                    )
                }

                composable(AdminRoute.Contents) {
                    AdminContentsSubScreen(
                        userRole = "superuser",
                        getAllContentUseCase = getAllContentUseCase,
                        insertContentUseCase = insertContentUseCase,
                        updateContentUseCase = updateContentUseCase,
                        deleteContentUseCase = deleteContentUseCase,
                        getGenresUseCase = getGenresUseCase
                    )
                }

                composable(AdminRoute.Genres) {
                    AdminGenresSubScreen(
                        getAllGenresUseCase = getGenresUseCase,
                        addGenreUseCase = addGenreUseCase,
                        updateGenreUseCase = updateGenreUseCase,
                        deleteGenreUseCase = deleteGenreUseCase
                    )
                }
            }
        }
    }
}

@Composable
private fun SidebarItem(
    item: NavigationItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else Color.Transparent
    val textColor = if (isSelected) Color.White else Color(0xFFCCCCCC)
    val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .pointerHoverIcon(PointerIcon.Hand)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Usamos el icono si existe
        if (item.icon != null) {
            Icon(
                painter = painterResource(item.icon),
                contentDescription = item.label,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
        }
        if (item.showLabel && item.label != null) {
            Text(
                text = item.label,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = fontWeight
            )
        }
    }
}