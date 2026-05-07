package org.six.series.ui.components.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.jetbrains.compose.resources.painterResource
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
fun AdaptiveTopBar(navController: NavController) {
    val contentColor = MaterialTheme.colorScheme.onPrimary

    // --- SIMPLE ERROR STATE ---
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // --- TEXT STYLE FOR THE LOGO ---
    val logoStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 32.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 6.sp,
        color = contentColor,
        shadow = Shadow(color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f), blurRadius = 12f)
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        // --- TOP BAR UI ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primary, Color.Transparent)))
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo
            Text(text = "SIX SERIES", style = logoStyle, modifier = Modifier.padding(horizontal = 24.dp))

            // Nav Items
            navItems.forEach { item ->
                if (item.icon == Res.drawable.ic_magnifying_Glass) {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Box(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            try {
                                navController.navigate(item.route) { launchSingleTop = true }
                            } catch (ex: Exception) {
                                errorMessage = "Error: Could not open ${item.label ?: "page"}"
                            }
                        }
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (item.icon != null) {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        if (item.icon != null && !item.label.isNullOrEmpty()) Spacer(Modifier.width(8.dp))
                        if (!item.label.isNullOrEmpty()) {
                            Text(
                                text = item.label,
                                color = contentColor,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // --- ERROR NOTIFICATION ---
        if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                ErrorNotification(
                    message = errorMessage!!,
                    onDismiss = { errorMessage = null }
                )
            }
        }
    }
}