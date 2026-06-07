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
import kotlinx.coroutines.flow.first
import org.six.series.application.usecases.content.GetContentUseCase
import org.six.series.application.usecases.favorite.AddFavoriteUseCase
import org.six.series.application.usecases.favorite.GetMyFavoritesUseCase
import org.six.series.application.usecases.favorite.RemoveFavoriteUseCase
import org.six.series.application.usecases.genre.GetContentByGenreUseCase
import org.six.series.application.usecases.genre.GetGenresUseCase
import org.six.series.application.usecases.history.GetHistoryUseCase
import org.six.series.application.usecases.history.SaveHistoryUseCase
import org.six.series.model.content.Content
import org.six.series.model.content.ContentType
import org.six.series.model.genre.Genre
import org.six.series.ui.appsettings.AppSettings

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val movies: List<Content>, val genres: List<Genre>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

class MainPageViewModel(
    private val context: PlatformContext,
    private val getContentUseCase: GetContentUseCase,
    private val getGenresUseCase: GetGenresUseCase,
    private val getContentByGenreUseCase: GetContentByGenreUseCase,
    private val imageLoader: ImageLoader,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val getMyFavoritesUseCase: GetMyFavoritesUseCase,
    private val saveHistoryUseCase: SaveHistoryUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val settings: AppSettings
) : ViewModel() {

    var uiState by mutableStateOf<MainUiState>(MainUiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set
    var searchResults by mutableStateOf<List<Content>>(emptyList())
        private set
    var isSearching by mutableStateOf(false)
        private set

    var moviesByGenreResult by mutableStateOf<List<Content>>(emptyList())
        private set
    var isLoadingGenreContent by mutableStateOf(false)
        private set
    var genreContentError by mutableStateOf<String?>(null)
        private set

    var favorites by mutableStateOf<List<Content>>(emptyList())
        private set
    var favoritesLoading by mutableStateOf(false)
        private set
    var favoriteIds by mutableStateOf<Set<String>>(emptySet())
        private set

    var recentlyWatched by mutableStateOf<List<Content>>(emptyList())
        private set
    var recentlyWatchedLoading by mutableStateOf(false)
        private set

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        uiState = MainUiState.Loading
        viewModelScope.launch {
            val contentDeferred = async { getContentUseCase() }
            val genresDeferred = async { getGenresUseCase() }
            val favoritesDeferred = async { getMyFavoritesUseCase() }

            val contentResult = contentDeferred.await()
            val genresResult = genresDeferred.await()
            val favoritesResult = favoritesDeferred.await()

            if (contentResult.isSuccess) {
                val allContent = contentResult.getOrThrow()
                val genres = genresResult.getOrElse { emptyList() }
                uiState = MainUiState.Success(allContent, genres)
                preloadImages(allContent.take(5))
                loadHistory(allContent)
            } else {
                uiState = MainUiState.Error("Error al cargar los datos del servidor")
            }

            favoritesResult.onSuccess { list ->
                favorites = list
                favoriteIds = list.mapNotNull { it.id }.toSet()
            }
        }
    }

    private suspend fun loadHistory(allContent: List<Content>) {
        val profileName = settings.profileName.first() ?: return
        recentlyWatchedLoading = true
        getHistoryUseCase(profileName).onSuccess { historyList ->
            recentlyWatched = historyList.mapNotNull { h ->
                allContent.find { it.title == h.title }
            }
        }
        recentlyWatchedLoading = false
    }

    val seriesList: List<Content>
        get() = (uiState as? MainUiState.Success)
            ?.movies?.filter { it.type == ContentType.Series } ?: emptyList()

    val moviesList: List<Content>
        get() = (uiState as? MainUiState.Success)
            ?.movies?.filter { it.type == ContentType.Movie } ?: emptyList()

    val documentariesList: List<Content>
        get() = (uiState as? MainUiState.Success)
            ?.movies?.filter { it.type == ContentType.Documentary } ?: emptyList()

    fun updateSearchQuery(query: String) {
        searchQuery = query
        if (query.isBlank()) searchResults = emptyList() else performSearch()
    }

    fun performSearch() {
        if (searchQuery.isBlank()) return
        val currentStatus = uiState as? MainUiState.Success ?: return
        viewModelScope.launch {
            isSearching = true
            searchResults = currentStatus.movies.filter {
                it.title.contains(searchQuery, ignoreCase = true)
            }
            isSearching = false
        }
    }

    fun clearSearchSelection() {
        updateSearchQuery("")
    }

    fun onGenreSelected(genreName: String) {
        viewModelScope.launch {
            isLoadingGenreContent = true
            genreContentError = null
            val result = getContentByGenreUseCase(genreName)
            if (result.isSuccess) {
                moviesByGenreResult = result.getOrThrow()
                if (moviesByGenreResult.isEmpty())
                    genreContentError = "No se encontraron contenidos para el género: $genreName"
            } else {
                moviesByGenreResult = emptyList()
                genreContentError =
                    result.exceptionOrNull()?.message ?: "Error al conectar con el servidor"
            }
            isLoadingGenreContent = false
        }
    }

    fun clearGenreSelection() {
        moviesByGenreResult = emptyList()
        isLoadingGenreContent = false
        genreContentError = null
    }

    fun isFavorite(contentId: String?): Boolean = contentId != null && contentId in favoriteIds

    fun toggleFavorite(content: Content) {
        val id = content.id ?: return
        viewModelScope.launch {
            if (isFavorite(id)) {
                removeFavoriteUseCase(content.title).onSuccess {
                    favoriteIds = favoriteIds - id
                    favorites = favorites.filter { it.id != id }
                }
            } else {
                addFavoriteUseCase(content.title).onSuccess {
                    favoriteIds = favoriteIds + id
                    favorites = favorites + content
                }
            }
        }
    }

    fun markAsWatched(content: Content) {
        viewModelScope.launch {
            val profileName = settings.profileName.first() ?: return@launch
            saveHistoryUseCase(profileName, content.title, 0)
            recentlyWatched = listOf(content) + recentlyWatched.filter { it.id != content.id }
        }
    }

    private fun preloadImages(movies: List<Content>) {
        movies.forEach { movie ->
            listOfNotNull(movie.coverURL, movie.logoURL, movie.portraitURL).forEach { url ->
                viewModelScope.launch(Dispatchers.IO) {
                    imageLoader.execute(ImageRequest.Builder(context).data(url).build())
                }
            }
        }
    }
}