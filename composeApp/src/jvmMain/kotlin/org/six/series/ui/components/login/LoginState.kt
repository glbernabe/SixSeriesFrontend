package org.six.series.ui.components.login

data class LoginState(
    // Campos del formulario
    val username: String= "",
    val password: String = "",

    // UI States
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isValid:Boolean = false,
    // Errores específicos de campo (validación local)
    val usernameError: String? = null,
    val passwordError: String? = null,

    // Error global (ej: "Credenciales incorrectas" o "No hay internet")
    val errorMessage: String? = null
)