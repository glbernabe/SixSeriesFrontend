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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.six.series.ui.components.basic.AdaptiveTopBar
import org.six.series.ui.components.basic.CarouselMovies


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent(rootNavController: NavController) {

    // This controller manages the content switching (Home, Movies, Series, etc.)
    // without affecting the Scaffold or the TopBar.
    val internalNavController = rememberNavController()
    val adaptiveInfo = currentWindowAdaptiveInfo()

    Scaffold(
        topBar = {
            // We pass both controllers: internal for section switching,
            // and root for the Logout action.
            AdaptiveTopBar(
                navController = internalNavController,
                rootNavController = rootNavController
            )
        }
    ) { innerPadding ->
        // The Box contains the NavHost which acts as the dynamic content area.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(innerPadding) // Avoids content being hidden under the TopBar
        ) {
            NavHost(
                navController = internalNavController,
                startDestination = MainRoutes.Principal
            ) {
                // Verification text
                composable(MainRoutes.Principal) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp)
                        ,
                        contentAlignment = Alignment.TopStart
                    ) {
                        CarouselMovies()
                    }
                }
                composable(MainRoutes.Movies) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: PELÍCULAS", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Series) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: SERIES", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Search) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: BÚSQUEDA", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Genres) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: GÉNEROS", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Profile) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: AJUSTES DE PERFIL", color = Color.White, fontSize = 24.sp)
                    }
                }

//                composable(MainRoutes.CambiarPassword) { Text("Cambiar Contraseña") }
//                composable(MainRoutes.CambiarImagen) { Text("Cambiar Imagen") }
//                composable(MainRoutes.ModificarUsuario) { Text("Modificar Usuario") }
//                composable(MainRoutes.BorrarUsuario) { Text("Borrar Usuario") }
            }
        }
    }
}