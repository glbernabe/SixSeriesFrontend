package org.six.series.ui.components.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDeep      = Color(0xFF0A0A0A)
private val BgCard      = Color(0xFF161616)
private val BorderIdle  = Color(0xFF2A2A2A)
private val TextPrimary = Color(0xFFEEEEEE)
private val TextMuted   = Color(0xFF888888)

@Composable
fun RegisterComponent(
    state: RegisterState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatedPasswordChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onRegister: () -> Unit,
    onCancel: () -> Unit,
    onLogin: () -> Unit = {}   // ← para el enlace "¿Ya tienes cuenta?"
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val focusManager = LocalFocusManager.current

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep),
        contentAlignment = Alignment.Center
    ) {
        // Radial glow igual que en Login
        Box(
            modifier = Modifier
                .size(520.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.09f), Color.Transparent)
                    )
                )
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 })
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Spacer(Modifier.height(32.dp))

                // Brand
                Text(
                    text = "SIX SERIES",
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 8.sp,
                        color = primaryColor,
                        shadow = Shadow(
                            color = primaryColor.copy(alpha = 0.5f),
                            offset = Offset(0f, 0f),
                            blurRadius = 16f
                        )
                    )
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Crea tu cuenta gratis",
                    fontSize = 13.sp,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.height(28.dp))

                // Card
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = BgCard,
                    tonalElevation = 0.dp,
                    shadowElevation = 24.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Crear cuenta",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        // Usuario
                        RegisterField(
                            value = state.username,
                            onValueChange = onUsernameChange,
                            label = "Nombre de usuario",
                            error = state.usernameError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            primaryColor = primaryColor
                        )

                        // Email
                        RegisterField(
                            value = state.email,
                            onValueChange = onEmailChange,
                            label = "Correo electrónico",
                            error = state.emailError,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            primaryColor = primaryColor
                        )

                        // Contraseña
                        RegisterField(
                            value = state.password,
                            onValueChange = onPasswordChange,
                            label = "Contraseña",
                            error = state.passwordError,
                            isPassword = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            primaryColor = primaryColor
                        )

                        // Repetir contraseña
                        RegisterField(
                            value = state.repeatePassword,
                            onValueChange = onRepeatedPasswordChange,
                            label = "Repetir contraseña",
                            error = state.repeatePasswordError,
                            isPassword = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus(); if (state.isValid) onRegister() }
                            ),
                            primaryColor = primaryColor
                        )

                        // Error general
                        AnimatedVisibility(visible = state.errorMessage != null) {
                            state.errorMessage?.let { msg ->
                                Text(
                                    text = msg,
                                    color = MaterialTheme.colorScheme.error,
                                    fontSize = 13.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = primaryColor,
                                modifier = Modifier.size(36.dp)
                            )
                        } else {
                            // Registrar
                            Button(
                                onClick = onRegister,
                                enabled = state.isValid,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = primaryColor.copy(alpha = 0.35f),
                                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    "Crear cuenta",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            HorizontalDivider(
                                color = Color(0xFF2A2A2A),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Enlace a login
                            TextButton(
                                onClick = onLogin,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "¿Ya tienes cuenta? Inicia sesión",
                                    color = primaryColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun RegisterField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    primaryColor: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, fontSize = 13.sp) },
            singleLine = true,
            isError = error != null,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = BorderIdle,
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedLabelColor = primaryColor,
                unfocusedLabelColor = TextMuted,
                errorLabelColor = MaterialTheme.colorScheme.error,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                errorTextColor = TextPrimary,
                cursorColor = primaryColor,
                focusedContainerColor = Color(0xFF1E1E1E),
                unfocusedContainerColor = Color(0xFF1A1A1A),
                errorContainerColor = Color(0xFF1A1A1A)
            )
        )
        AnimatedVisibility(visible = error != null) {
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}