package org.six.series.ui.components.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            startDestination = MainRoutes.Perfil
        ) {
            composable(MainRoutes.Perfil) { Text("Perfil Usuario") }
//            composable(MainRoutes.CambiarPassword) { Text("Cambiar Contraseña") }
//            composable(MainRoutes.CambiarImagen) { Text("Cambiar Imagen") }
//            composable(MainRoutes.ModificarUsuario) { Text("Modificar Usuario") }
//            composable(MainRoutes.BorrarUsuario) { Text("Borrar Usuario") }
        }
    }

    // PARTE DE MÓVIL
    if (adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        /*
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        items.forEach { item ->
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate(item.second) },
                                icon = {
                                    Icon(
                                        imageVector = item.first,
                                        contentDescription = item.second
                                    )
                                }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(Modifier.padding(innerPadding)) {
                    navegador()
                }
            }
        */
    } else {

        // PARTE DE ESCRITORIO
//        PermanentNavigationDrawer(
//            drawerContent = {
//                PermanentDrawerSheet(
//                    Modifier.width(100.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxHeight()
//                            .padding(vertical = 16.dp),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//
//                        Spacer(Modifier.height(16.dp))
//
////                        items.forEach { item ->
////                            NavigationDrawerItem(
////                                icon = {
////                                    Box(
////                                        modifier = Modifier.fillMaxWidth(),
////                                        contentAlignment = Alignment.Center
////                                    ) {
////                                        Icon(
////                                            item.first,
////                                            contentDescription = item.second,
////                                            tint = MaterialTheme.colorScheme.primary
////                                        )
////                                    }
////                                },
////                                label = { },
////                                selected = false,
////                                onClick = { navController.navigate(item.second) },
////                                modifier = Modifier.padding(vertical = 4.dp)
////                            )
////                        }
//                    }
//                }
//            },
//            content = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp)
//                        .height(600.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//
//                }
//            }
//        )
        Scaffold(
            topBar = {
                AdaptiveTopBar(
                    navController = navController
                )
            }
        ) { innerPadding -> navegador()}


    }
}
