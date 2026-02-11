package ies.sequeros.dam.pmdm.gestionperifl.ui.appsettings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppSettings {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }
    fun setDarkMode() {
        _isDarkMode.value = true
    }
    fun setLightMode() {
        _isDarkMode.value = false
    }
}