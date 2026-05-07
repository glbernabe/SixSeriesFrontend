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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.six.series.AppRoute
import org.six.series.ui.components.register.RegisterComponent
import org.six.series.ui.components.register.RegisterFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController,   // añadimos navController
    onRegister: () -> Unit,
    onCancel: () -> Unit,
) {

    val viewModel = koinViewModel<RegisterFormViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isRegisterSuccess) {
        if (state.isRegisterSuccess) {
            onRegister()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        RegisterComponent(
            state = state,
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onRepeatedPasswordChange = viewModel::onRepeatPasswordChange,
            onEmailChange = viewModel::onEmailChange,
            onRegister = viewModel::register,
            onCancel = onCancel
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón simple para volver a Login
        OutlinedButton(
            onClick = { navController.navigate(AppRoute.Login) },
            modifier = Modifier.height(40.dp)
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión", fontSize = 14.sp)
        }
    }
}