package org.six.series.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import org.six.series.ui.components.basic.AdaptiveTopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent() {

    val navController = rememberNavController()
    val adaptiveInfo = currentWindowAdaptiveInfo()



    val navegador: @Composable () -> Unit = {
        NavHost(
            navController = navController,
            startDestination = MainRoutes.Principal
        ) {
            composable(MainRoutes.Principal) { Text("Perfil Usuario") }
//            composable(MainRoutes.CambiarPassword) { Text("Cambiar Contraseña") }
//            composable(MainRoutes.CambiarImagen) { Text("Cambiar Imagen") }
//            composable(MainRoutes.ModificarUsuario) { Text("Modificar Usuario") }
//            composable(MainRoutes.BorrarUsuario) { Text("Borrar Usuario") }
        }
    }

    Scaffold(
        topBar = {
            AdaptiveTopBar(
                navController = navController
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.onBackground)) {
            navegador()
        }
    }
}
