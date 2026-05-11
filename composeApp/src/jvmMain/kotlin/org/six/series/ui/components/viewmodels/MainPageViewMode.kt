package org.six.series.ui.components.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.model.content.Content
import org.six.series.ui.components.login.LoginState

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val movies: List<Content>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

class MainPageViewModel(
    private val getAllContentUseCase: GetContentUseCase
) : ViewModel() {

    var uiState by mutableStateOf<MainUiState>(MainUiState.Loading)
        private set

    init {
        loadMovies()
    }

    fun loadMovies() {
        uiState = MainUiState.Loading
        viewModelScope.launch {
            val result = getAllContentUseCase()

            result.onSuccess { list ->
                uiState = if (list.isEmpty()) {
                    MainUiState.Error("No hay películas disponibles en este momento.")
                } else {
                    MainUiState.Success(list)
                }
            }.onFailure { error ->
                uiState = MainUiState.Error(
                    error.message ?: "Error desconocido al conectar con el servidor"
                )
            }
        }
    }

    fun onMovieSelected(movie: Content) {
        // Navigation logic for player would go here
        println("Selected movie: ${movie.title}")
    }
}