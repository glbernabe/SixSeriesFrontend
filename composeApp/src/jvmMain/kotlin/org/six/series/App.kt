package org.six.series

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import org.six.series.ui.appsettings.AppViewModel
import org.six.series.ui.components.screens.LoginScreen
import org.six.series.ui.components.screens.MainScreen
import org.six.series.ui.components.screens.RegisterScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val appViewModel: AppViewModel = koinViewModel()
    val navController = rememberNavController()
    val startDestination by appViewModel.startDestination.collectAsState()

    // Si aún no sabemos a dónde ir, mostrar cargando
    if (startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    AppTheme(appViewModel.isDarkMode.collectAsState()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NavHost(
                navController = navController,
                startDestination = startDestination!!
            ) {
                composable(AppRoute.login) {
                    LoginScreen(
                        navController = navController,
                        onLogin = { navController.navigate(AppRoute.main) },
                        onCancel = { /* TODO: cerrar app o limpiar */ }
                    )
                }

                composable(AppRoute.register) {
                    RegisterScreen(
                        navController = navController,
                        onRegister = { navController.navigate(AppRoute.login) },
                        onCancel = { navController.popBackStack() }
                    )
                }

                composable(AppRoute.main) {
                    MainScreen()
                }
            }
        }
    }
}