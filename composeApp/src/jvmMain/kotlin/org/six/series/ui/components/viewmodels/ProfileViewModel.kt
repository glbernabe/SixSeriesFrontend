package org.six.series.ui.components.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.six.series.application.usecases.profile.CreateProfileUseCase
import org.six.series.application.usecases.profile.GetMyProfilesUseCase
import org.six.series.application.usecases.profile.UpdateProfileUseCase
import org.six.series.model.profile.Profile
import org.six.series.model.profile.ProfileUpdateRequest
import org.six.series.ui.appsettings.AppViewModel

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    object NoProfile : ProfileUiState()
    data class Success(val profile: Profile) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(
    private val getMyProfilesUseCase: GetMyProfilesUseCase,
    private val createProfileUseCase: CreateProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val appViewModel: AppViewModel
) : ViewModel() {

    // Cambiado el estado inicial a Loading para evitar parpadeos con datos residuales anteriores
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _saveSuccess = MutableStateFlow<String?>(null)
    val saveSuccess: StateFlow<String?> = _saveSuccess.asStateFlow()

    // load first profile from the list
    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            getMyProfilesUseCase()
                .onSuccess { profiles ->
                    val profile = profiles.firstOrNull()
                    if (profile != null) {
                        _uiState.value = ProfileUiState.Success(profile)
                        profile.profileColor?.let { hex ->
                            runCatching {
                                val colorLong = hex.removePrefix("#").toLong(16) or 0xFF000000L
                                appViewModel.updateAppColor(colorLong)
                            }
                        }
                    } else {
                        _uiState.value = ProfileUiState.NoProfile
                    }
                }
                .onFailure {
                    _uiState.value = ProfileUiState.Error("Error al cargar el perfil")
                }
        }
    }

    fun saveChanges(newName: String, newColorLong: Long) {
        val state = _uiState.value as? ProfileUiState.Success ?: return

        if (newName.isBlank()) {
            _saveSuccess.value = "El nombre no puede estar vacío"
            return
        }

        viewModelScope.launch {
            val hexColor = "#" + (newColorLong and 0xFFFFFF).toString(16).padStart(6, '0').uppercase()

            updateProfileUseCase(
                state.profile.id,
                ProfileUpdateRequest(name = newName, profileColor = hexColor)
            ).onSuccess { updated ->
                _uiState.value = ProfileUiState.Success(updated)

                appViewModel.updateAppColor(newColorLong)

                _saveSuccess.value = "Perfil guardado correctamente"
            }.onFailure {
                _saveSuccess.value = "Error al guardar el perfil"
            }
        }
    }

    // create a new profile with default gray color
    fun createProfile(name: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            createProfileUseCase(name)
                .onSuccess { loadProfile() }
                .onFailure { _uiState.value = ProfileUiState.Error("Error al crear el perfil") }
        }
    }

    // ── Cleans all the profiles if the user logout ──
    fun clearState() {
        _uiState.value = ProfileUiState.Loading
        _saveSuccess.value = null
    }

    fun dismissSaveMessage() { _saveSuccess.value = null }
}