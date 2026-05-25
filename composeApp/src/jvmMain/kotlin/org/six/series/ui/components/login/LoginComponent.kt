package org.six.series.ui.components.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
fun LoginComponent(
    state: LoginState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onCancel: () -> Unit,
    onRegister: () -> Unit          // ← nuevo
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
        // Radial glow
        Box(
            modifier = Modifier
                .size(480.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(primaryColor.copy(alpha = 0.10f), Color.Transparent)
                    )
                )
        )

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 6 })
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
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
                    "Tu contenido, cuando quieras",
                    fontSize = 13.sp,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.height(36.dp))

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
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Iniciar sesión",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )

                        StyledTextField(
                            value = state.username,
                            onValueChange = onUsernameChange,
                            label = "Nombre de usuario",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            primaryColor = primaryColor
                        )

                        StyledTextField(
                            value = state.password,
                            onValueChange = onPasswordChange,
                            label = "Contraseña",
                            isPassword = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus(); onLoginClick() }
                            ),
                            primaryColor = primaryColor
                        )

                        // Error
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
                            // Entrar
                            Button(
                                onClick = onLoginClick,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primaryColor,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                )
                            ) {
                                Text("Entrar", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp)
                            }

                            // Separador
                            HorizontalDivider(
                                color = Color(0xFF2A2A2A),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            // Registrarse
                            TextButton(
                                onClick = onRegister,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "¿No tienes cuenta? Regístrate",
                                    color = primaryColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    primaryColor: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 13.sp) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = primaryColor,
            unfocusedBorderColor = BorderIdle,
            focusedLabelColor = primaryColor,
            unfocusedLabelColor = TextMuted,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            cursorColor = primaryColor,
            focusedContainerColor = Color(0xFF1E1E1E),
            unfocusedContainerColor = Color(0xFF1A1A1A)
        )
    )
}