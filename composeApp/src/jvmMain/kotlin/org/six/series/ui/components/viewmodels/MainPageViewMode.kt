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
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.model.content.Content
import org.six.series.model.genre.Genre

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(
        val movies: List<Content>,
        val genres: List<Genre>
    ) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

class MainPageViewModel(
    private val context: PlatformContext,
    private val getContentUseCase: GetContentUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val imageLoader: ImageLoader
) : ViewModel() {

    var uiState by mutableStateOf<MainUiState>(MainUiState.Loading)
        private set

    // --- Search States ---
    var searchQuery by mutableStateOf("")
        private set

    var searchResults by mutableStateOf<List<Content>>(emptyList())
        private set

    var isSearching by mutableStateOf(false)
        private set

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        uiState = MainUiState.Loading

        viewModelScope.launch {
            val contentDeferred = async { getContentUseCase() }
            val genresDeferred = async { getGenresUseCase() }

            val contentResult = contentDeferred.await()
            val genresResult = genresDeferred.await()

            if (contentResult.isSuccess && genresResult.isSuccess) {
                val movies = contentResult.getOrThrow()
                val genres = genresResult.getOrThrow()

                uiState = MainUiState.Success(movies, genres)

                preloadImages(movies.take(5))
            } else {
                uiState = MainUiState.Error("Error al cargar los datos del servidor")
            }
        }
    }

    // --- Research Function ---
    fun updateSearchQuery(query: String) {
        searchQuery = query
        if (query.isBlank()) {
            searchResults = emptyList()
        } else {
            // Will search automatically while you type
            performSearch()
        }
    }

    fun performSearch() {
        if (searchQuery.isBlank()) return

        uiState.let { currentStatus ->
            // It loads the current content catalog before searching for better experience
            if (currentStatus is MainUiState.Success) {
                viewModelScope.launch {
                    isSearching = true

                    // Simulates an async backend or local repository filter operation matching titles
                    val filtered = currentStatus.movies.filter { content ->
                        content.title.contains(searchQuery, ignoreCase = true)
                    }

                    searchResults = filtered
                    isSearching = false
                }
            }
        }
    }

    private fun preloadImages(movies: List<Content>) {
        movies.forEach { movie ->
            val urls = listOfNotNull(movie.coverURL, movie.logoURL, movie.portraitURL)
            urls.forEach { imageUrl ->
                viewModelScope.launch(Dispatchers.IO) {
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .build()
                    imageLoader.execute(request)
                }
            }
        }
    }
}