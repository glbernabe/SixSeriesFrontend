package org.six.series.ui.components.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.six.series.application.commands.RegisterCommand
import org.six.series.application.usecases.user.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterFormViewModel(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()
    val isFormValid = MutableStateFlow(false)

    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(
                username = username,
                usernameError = if (username.length < 8) "Debe tener entre 8 y 64 caracteres" else null
            )
        }
        validForm()
    }

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email,
                emailError = if (email.contains("@")) null else "Email no válido"
            )
        }
        validForm()
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = validatePassword(password)
            )
        }
        validatePasswords()
        validForm()
    }

    fun onRepeatPasswordChange(repeatPassword: String) {
        _state.update {
            it.copy(
                repeatePassword = repeatPassword
            )
        }
        validatePasswords()
        validForm()
    }

    private fun validatePasswords() {
        _state.update { state ->
            state.copy(
                repeatePasswordError =
                    if (state.repeatePassword == state.password)
                        null
                    else
                        "Deben coincidir las contraseñas"
            )
        }
    }


    //Válida si hay en la contraseña una de esas caracteristicas, y si no cumple con una, suelta un eerror
    private fun validatePassword(password: String): String? {
        if (password.length !in 3..32) return "Debe tener entre 6 y 32 caracteres"
        return null
//        val upper = password.any { it.isUpperCase() }
//        val lower = password.any { it.isLowerCase() }
//        val digit = password.any { it.isDigit() }
//        val special = password.any { !it.isLetterOrDigit() }
//
//        return if (upper && lower && digit && special) null
//        else "Debe incluir mayúscula, minúscula, número y carácter especial"
    }

    private fun validForm() {
        val s = _state.value
        isFormValid.value = s.email.isNotBlank() &&
                s.username.isNotBlank() &&
                s.password.isNotBlank() &&
                s.emailError == null &&
                s.passwordError == null &&
                s.repeatePasswordError == null
        _state.value = state.value.copy(isValid = isFormValid.value)

    }

    fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                _state.value = state.value.copy(isLoading = true)
                val registerCommand =
                    RegisterCommand(
                        email = _state.value.email,
                        password = _state.value.password,
                        username = _state.value.username,
                    )
                registerUseCase.register(registerCommand)
                _state.update { it.copy(isRegisterSuccess = true) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error con el registro..."
                    )
                }
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }


}