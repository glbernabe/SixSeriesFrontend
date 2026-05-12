package org.six.series.ui.components.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import kotlinx.coroutines.*
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.model.content.Content

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val movies: List<Content>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

class MainPageViewModel(
    private val context: PlatformContext,
    private val getAllContentUseCase: GetContentUseCase,
    private val imageLoader: ImageLoader
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

                if (list.isEmpty()) {
                    uiState = MainUiState.Error(
                        "No hay películas disponibles en este momento."
                    )
                    return@onSuccess
                }

                try {
                    // LOADS THE IMAGES BEFORE IT SHOWS
                    val carouselTop = list.take(5)

                    coroutineScope {

                        val jobs = carouselTop.flatMap { movie ->

                            listOfNotNull(
                                movie.coverURL,
                                movie.logoURL,
                                movie.portraitURL
                            ).map { imageUrl ->

                                async(Dispatchers.IO) {

                                    val request = ImageRequest.Builder(context)
                                        .data(imageUrl)
                                        .build()

                                    imageLoader.execute(request)
                                }
                            }
                        }

                        jobs.awaitAll()
                    }

                    uiState = MainUiState.Success(list)

                } catch (e: Exception) {

                    uiState = MainUiState.Error(
                        e.message ?: "Error cargando imágenes"
                    )
                }

            }.onFailure { error ->

                uiState = MainUiState.Error(
                    error.message ?: "Error desconocido al conectar con el servidor"
                )
            }
        }
    }
}