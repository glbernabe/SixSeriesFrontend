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

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Error(""))
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
                        profile.themeColor?.let { hex ->
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

    // update app color and save it to the profile on the server
    fun updateColor(colorLong: Long) {
        appViewModel.updateAppColor(colorLong)
        val state = _uiState.value
        if (state is ProfileUiState.Success) {
            viewModelScope.launch {
                val hexColor = "#%06X".format(colorLong and 0xFFFFFF)
                updateProfileUseCase(
                    state.profile.id,
                    ProfileUpdateRequest(name = state.profile.name, themeColor = hexColor)
                )
            }
        }
    }

    // save name changes
    fun saveChanges(newName: String) {
        val state = _uiState.value as? ProfileUiState.Success ?: return
        viewModelScope.launch {
            updateProfileUseCase(
                state.profile.id,
                ProfileUpdateRequest(name = newName)
            ).onSuccess { updated ->
                _uiState.value = ProfileUiState.Success(updated)
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

    fun dismissSaveMessage() { _saveSuccess.value = null }
}