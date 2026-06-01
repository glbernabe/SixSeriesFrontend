package org.six.series.ui.components.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.six.series.application.usecases.content.GetEpisodesUseCase
import org.six.series.model.content.Content
import org.six.series.model.content.ContentType
import org.six.series.model.content.Episode

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val content: Content, val episodes: List<Episode>) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class DetailViewModel(
    private val getEpisodesUseCase: GetEpisodesUseCase
) : ViewModel() {

    var uiState by mutableStateOf<DetailUiState>(DetailUiState.Loading)
        private set

    fun load(content: Content) {
        println("LOAD - type: ${content.type}, id: ${content.id}")
        if (content.type != ContentType.Series) {
            uiState = DetailUiState.Success(content, emptyList())
            return
        }
        viewModelScope.launch {
            uiState = DetailUiState.Loading
            getEpisodesUseCase(content.id ?: return@launch)
                .onSuccess {
                    println("EPISODIOS: ${it.size}")
                    uiState = DetailUiState.Success(content, it)
                }
                .onFailure {
                    println("ERROR: ${it.message}")
                    uiState = DetailUiState.Error(it.message ?: "Error")
                }
        }
    }
    }