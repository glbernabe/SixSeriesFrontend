package org.six.series

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.six.series.ui.appsettings.AppViewModel
import org.six.series.ui.components.screens.LoginScreen
import org.six.series.ui.components.screens.MainScreen
import org.six.series.ui.components.screens.ProfileSelectorScreen
import org.six.series.ui.components.screens.RegisterScreen
import org.six.series.ui.components.screens.SubscriptionScreen
import org.six.series.ui.components.viewmodels.ProfileViewModel

@Composable
fun App() {
    val appViewModel: AppViewModel = koinViewModel()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val navController = rememberNavController()
    val startDestination by appViewModel.startDestination.collectAsState()
    val appColorLong by appViewModel.appColor.collectAsState()

    if (startDestination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    AppTheme(selectedProfileColor = Color(appColorLong)) {
        Column(
            modifier = Modifier
                .background(Color(0xFF0D0D0D))
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
                        onLogin = { navController.navigate(AppRoute.ProfileSelector) },
                        onCancel = { }
                    )
                }

                composable(AppRoute.Register) {
                    RegisterScreen(
                        navController = navController,
                        onRegister = { navController.navigate(AppRoute.Login) },
                        onCancel = { navController.popBackStack() }
                    )
                }

                composable(AppRoute.ProfileSelector) {
                    ProfileSelectorScreen(
                        viewModel = profileViewModel,
                        onProfileSelected = { profile ->
                            profile.profileColor?.let { hex ->
                                try {
                                    val colorLong = hex.removePrefix("#").toLong(16) or 0xFF000000L
                                    appViewModel.setColorFromProfile(colorLong)
                                } catch (e: Exception) { }
                            }
                            navController.navigate(AppRoute.Main) {
                                popUpTo(AppRoute.ProfileSelector) { inclusive = true }
                            }
                        },
                        onManageSubscription = {
                            navController.navigate(AppRoute.SubscriptionManager)
                        },
                        onGoToLogin = {
                            navController.navigate(AppRoute.Login) { popUpTo(0) }
                        }
                    )
                }

                composable(AppRoute.Main) {
                    MainScreen(navController)
                }

                composable(AppRoute.SubscriptionManager) {
                    SubscriptionScreen(
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}