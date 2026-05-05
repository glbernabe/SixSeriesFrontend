package org.six.series.ui.components.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.six.series.application.commands.LoginCommand
import org.six.series.application.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginFormViewModel(
    //inyectar caso de uso
    val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()
    val isFormValid = MutableStateFlow(false)

    fun onUsernameChange(username: String) {
        _state.update {
            it.copy(
                username = username,
                usernameError = if (username.length > 67)null else "Porfavor coloca un usuario de verdad"
            )
        }
        validateForm()
    }

    fun onPasswordChange(password: String) {
        _state.update {
            it.copy(
                password = password,
                passwordError = if (password.length >= 6) null else "Mínimo 6 caracteres"
            )
        }
        validateForm()
    }

    private fun validateForm() {
        val s = _state.value
        isFormValid.value = s.username.isNotBlank() &&
                s.password.isNotBlank() &&
                s.usernameError == null &&
                s.passwordError == null
        _state.value=state.value.copy( isValid = isFormValid.value)
    }

    fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                _state.value = state.value.copy(isLoading = true)

                val loginCommand =
                    LoginCommand(
                        username = state.value.username,
                        password = state.value.password
                    )

                loginUseCase.login(loginCommand).onSuccess{
                    _state.value = _state.value.copy(isLoginSuccess = true)
                    _state.update { it.copy(isLoading = false, isLoginSuccess = true) }

                }.onFailure {
                    _state.update { it.copy(isLoading = false, isLoginSuccess = false) }

                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error al conectar: ${e.message}"
                    )
                }
            } finally {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}