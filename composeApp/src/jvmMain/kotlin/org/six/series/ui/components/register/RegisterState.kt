package org.six.series.ui.components.register

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val repeatePassword: String = "",

    // UI States
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val isValid: Boolean = false,
    // Errores específicos de campo (validación local)

    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val repeatePasswordError: String? = null,

    // Error global (ej: "Credenciales incorrectas" o "No hay internet")
    val errorMessage: String? = null

)
