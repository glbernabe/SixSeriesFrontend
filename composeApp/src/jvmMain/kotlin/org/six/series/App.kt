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
import androidx.compose.ui.graphics.Color
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

    // We obtain the Stateflow of the color and change it to State of Compose
    val appColorLong by appViewModel.currentHexColor.collectAsState()

    if (startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    // Transform the State to a Color
    AppTheme(
        selectedProfileColor = Color(appColorLong)
    ) {
        Column(
            modifier = Modifier
                // Depending on the profile, the color of the app colors changes
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination!!
            ) {
                composable(AppRoute.Login) {
                    LoginScreen(
                        navController = navController,
                        onLogin = { navController.navigate(AppRoute.Main) },
                        onCancel = { /* TODO: cerrar app o limpiar */ }
                    )
                }

                composable(AppRoute.Register) {
                    RegisterScreen(
                        navController = navController,
                        onRegister = { navController.navigate(AppRoute.Login) },
                        onCancel = { navController.popBackStack() }
                    )
                }

                composable(AppRoute.Main) {
                    MainScreen(navController)
                }
            }
        }
    }
}