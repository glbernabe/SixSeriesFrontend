package org.six.series.ui.components.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.six.series.model.NavigationItem
import org.six.series.ui.components.main.MainRoutes


// ALL THE ICONS FOR THE UI ON THE TOP BAR
val navItems = listOf(
    NavigationItem(Icons.Default.Person, MainRoutes.Perfil, "Perfil"),
    NavigationItem(Icons.Default.Lock, MainRoutes.CambiarPassword, "Seguridad"),
    NavigationItem(Icons.Default.Photo, MainRoutes.CambiarImagen, "Imagen"),
    NavigationItem(Icons.Default.Edit, MainRoutes.ModificarUsuario, "Editar"),
    NavigationItem(Icons.Default.Delete, MainRoutes.BorrarUsuario, "Eliminar")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveTopBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                IconButton(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}