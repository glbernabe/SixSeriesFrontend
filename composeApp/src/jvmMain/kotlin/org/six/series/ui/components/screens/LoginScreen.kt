package org.six.series.ui.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.six.series.AppRoute
import org.six.series.ui.components.login.LoginComponent
import org.six.series.ui.components.login.LoginFormViewModel

@Composable
fun LoginScreen(
    navController: NavController,   // lo recibimos para navegar
    onLogin: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel = koinViewModel<LoginFormViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) {
            onLogin()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        LoginComponent(
            state = state,
            onEmailChange = viewModel::onEmailChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = { viewModel.login() },
            onCancel = onCancel
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón simple para ir a Register
        OutlinedButton(
            onClick = { navController.navigate(AppRoute.register) },
            modifier = Modifier.height(40.dp)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}