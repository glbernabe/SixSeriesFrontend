package org.six.series.ui.components.basic.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import org.six.series.application.usecases.genre.GetContentByGenreUseCase
import org.six.series.model.content.Content

// Sealed class para controlar el estado de esta pantalla específica
sealed class GenreUiState {
    object Loading : GenreUiState()
    data class Success(val movies: List<Content>) : GenreUiState()
    data class Error(val message: String) : GenreUiState()
}

class GenreDetailViewModel(
    private val genreName: String,
    private val getContentByGenreUseCase: GetContentByGenreUseCase
) : ViewModel() {

    var uiState by mutableStateOf<GenreUiState>(GenreUiState.Loading)
        private set

    init {
        loadContent()
    }

    private fun loadContent() {
        uiState = GenreUiState.Loading
        viewModelScope.launch {
            val result = getContentByGenreUseCase(genreName)
            uiState = if (result.isSuccess) {
                GenreUiState.Success(result.getOrThrow())
            } else {
                GenreUiState.Error(result.exceptionOrNull()?.message ?: "Error desconocido")
            }
        }
    }
}