package org.six.series.ui.components.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginComponent (state: LoginState,
                    onUsernameChange: (String) -> Unit,
                    onPasswordChange: (String) -> Unit,
                    onLoginClick: () -> Unit,
                    onCancel: () -> Unit)
{
    val viewModel = koinViewModel<LoginFormViewModel>()

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .widthIn(max = 400.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo de Email
            OutlinedTextField(
                value = state.username,
                onValueChange = { onUsernameChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                //isError = state.emailError != null,
                //supportingText = {
                //    state.emailError?.let { Text(it) }
                //},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = state.password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                //isError = state.passwordError != null,
                //supportingText = {
                //    state.passwordError?.let { Text(it) }
                //},
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            // Error General
            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre botones
                ) {
                    // Botón de Cancelar (Secundario)
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancelar")
                    }

                    // Botón de Login (Primario)
                    Button(
                        onClick = {
                            onLoginClick()
                            viewModel.login()

                        },
                        modifier = Modifier.weight(1f),
                        //enabled = state.isValid && !state.isLoading,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Entrar")
                    }

                    Spacer(modifier = Modifier.width(24.dp))
                }
                if (state.errorMessage != null) {
                    val shortMsg = state.errorMessage.take(50)
                    Text(
                        text = shortMsg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}