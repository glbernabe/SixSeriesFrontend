package org.six.series.ui.components.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import org.koin.compose.viewmodel.koinViewModel
import org.six.series.AppRoute
import org.six.series.ui.components.register.RegisterComponent
import org.six.series.ui.components.register.RegisterFormViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    onRegister: () -> Unit,
    onCancel: () -> Unit,
) {
    val viewModel = koinViewModel<RegisterFormViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isRegisterSuccess) {
        if (state.isRegisterSuccess) onRegister()
    }

    // RegisterComponent gestiona todo el layout y fondo
    RegisterComponent(
        state = state,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatedPasswordChange = viewModel::onRepeatPasswordChange,
        onEmailChange = viewModel::onEmailChange,
        onRegister = viewModel::register,
        onCancel = onCancel,
        onLogin = { navController.navigate(AppRoute.Login) }
    )
}