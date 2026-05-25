package org.six.series.ui.components.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.six.series.AppRoute
import org.six.series.ui.components.login.LoginComponent
import org.six.series.ui.components.login.LoginFormViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    onLogin: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel = koinViewModel<LoginFormViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) onLogin()
    }

    // LoginComponent now owns the full-screen background + layout
    LoginComponent(
        state = state,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLoginClick = { viewModel.login() },
        onCancel = onCancel,
        onRegister = { navController.navigate(AppRoute.Register) }
    )

    // Register link positioned via LoginScreen so it stays outside the card
    // Rendered by LoginComponent's column already — but if you need it here:
    // (keep this block only if RegisterButton is NOT inside LoginComponent)
    /*
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        OutlinedButton(
            onClick = { navController.navigate(AppRoute.Register) },
            modifier = Modifier.padding(bottom = 32.dp)
        ) { Text("¿No tienes cuenta? Regístrate") }
    }
    */
}