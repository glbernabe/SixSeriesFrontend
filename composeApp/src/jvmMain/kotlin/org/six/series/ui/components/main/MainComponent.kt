package org.six.series.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform.getKoin
import org.six.series.model.content.Content
import org.six.series.model.content.ContentType
import org.six.series.ui.components.basic.AdaptiveTopBar
import org.six.series.ui.components.basic.HeroScreen
import org.six.series.ui.components.basic.content.ContentListScreen
import org.six.series.ui.components.basic.content.FavoritesScreen
import org.six.series.ui.components.basic.content.SearchContentScreen
import org.six.series.ui.components.basic.genre.GenreDetailScreen
import org.six.series.ui.components.basic.genre.GenresGrid
import org.six.series.ui.components.screens.DetailScreen
import org.six.series.ui.components.screens.ProfileScreen
import org.six.series.ui.components.screens.SubscriptionScreen
import org.six.series.ui.components.screens.VideoPlayerScreen
import org.six.series.ui.components.viewmodels.DetailViewModel
import org.six.series.ui.components.viewmodels.MainPageViewModel
import org.six.series.ui.components.viewmodels.MainUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(rootNavController: NavController) {
    val internalNavController = rememberNavController()
    val context = LocalPlatformContext.current
    val imageLoader = SingletonImageLoader.get(context)
    var selectedContent by remember { mutableStateOf<Content?>(null) }
    var selectedGenreName by remember { mutableStateOf<String?>(null) }

    val viewModel = remember {
        MainPageViewModel(
            context = context,
            imageLoader = imageLoader,
            getContentUseCase = getKoin().get(),
            getGenresUseCase = getKoin().get(),
            getContentByGenreUseCase = getKoin().get(),
            addFavoriteUseCase = getKoin().get(),
            removeFavoriteUseCase = getKoin().get(),
            getMyFavoritesUseCase = getKoin().get(),
            saveHistoryUseCase = getKoin().get(),
            getHistoryUseCase = getKoin().get(),
            settings = getKoin().get()
        )
    }

    var contentToPlay by remember { mutableStateOf<Content?>(null) }

    Scaffold(
        topBar = {
            AdaptiveTopBar(
                navController = internalNavController,
                rootNavController = rootNavController
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(innerPadding)
        ) {
            NavHost(
                navController = internalNavController,
                startDestination = MainRoutes.Principal
            ) {
                composable(MainRoutes.Principal) {
                    HeroScreen(
                        state = viewModel.uiState,
                        onPlayContent = { content ->
                            selectedContent = content
                            internalNavController.navigate(MainRoutes.Detail)
                        }
                    )
                }

                composable(MainRoutes.Movies) {
                    ContentListScreen(
                        title = "Películas",
                        items = viewModel.moviesList,
                        isLoading = viewModel.uiState is MainUiState.Loading,
                        onItemClick = { content ->
                            selectedContent = content
                            internalNavController.navigate(MainRoutes.Detail)
                        }
                    )
                }

                composable(MainRoutes.Series) {
                    ContentListScreen(
                        title = "Series",
                        items = viewModel.seriesList,
                        isLoading = viewModel.uiState is MainUiState.Loading,
                        onItemClick = { content ->
                            selectedContent = content
                            internalNavController.navigate(MainRoutes.Detail)
                        }
                    )
                }

                composable(MainRoutes.Search) {
                    LaunchedEffect(Unit) { viewModel.clearSearchSelection() }
                    SearchContentScreen(
                        searchQuery = viewModel.searchQuery,
                        searchResults = viewModel.searchResults,
                        isSearching = viewModel.isSearching,
                        onQueryChange = { viewModel.updateSearchQuery(it) },
                        onMovieClick = { contentToPlay = it }
                    )
                }

                composable(MainRoutes.Genres) {
                    LaunchedEffect(Unit) {
                        selectedGenreName = null
                        viewModel.clearGenreSelection()
                    }
                    GenresGrid(
                        state = viewModel.uiState,
                        onGenreClick = { genreName ->
                            selectedGenreName = genreName
                            viewModel.onGenreSelected(genreName)
                            internalNavController.navigate("genres_detail") {
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable("genres_detail") {
                    val genreName = selectedGenreName ?: return@composable
                    GenreDetailScreen(
                        genreName = genreName,
                        content = viewModel.moviesByGenreResult,
                        isLoading = viewModel.isLoadingGenreContent,
                        errorMessage = viewModel.genreContentError,
                        onMovieClick = { content ->
                            selectedContent = content
                            internalNavController.navigate(MainRoutes.Detail)
                        },
                        onBackClick = {
                            selectedGenreName = null
                            viewModel.clearGenreSelection()
                            internalNavController.navigate(MainRoutes.Genres) {
                                popUpTo(MainRoutes.Principal) { saveState = false }
                                launchSingleTop = true
                                restoreState = false
                            }
                        }
                    )
                }

                composable(MainRoutes.Favorites) {
                    FavoritesScreen(
                        favorites = viewModel.favorites,
                        favoritesLoading = viewModel.favoritesLoading,
                        recentlyWatched = viewModel.recentlyWatched,
                        recentlyWatchedLoading = false,
                        onItemClick = { content ->
                            selectedContent = content
                            internalNavController.navigate(MainRoutes.Detail)
                        },
                        onRemoveFavorite = { content ->
                            viewModel.toggleFavorite(content)
                        }
                    )
                }

                composable(MainRoutes.Profile) { ProfileScreen() }
                composable(MainRoutes.Subscription) { SubscriptionScreen() }

                composable(MainRoutes.Detail) {
                    val content = selectedContent ?: return@composable
                    val detailViewModel: DetailViewModel = koinViewModel()
                    DetailScreen(
                        content = content,
                        viewModel = detailViewModel,
                        isFavorite = viewModel.isFavorite(content.id),
                        onToggleFavorite = { viewModel.toggleFavorite(content) },
                        onPlayEpisode = { ep ->
                            val c = Content(
                                id = null,
                                title = ep.title,
                                description = ep.description ?: "",
                                ageRating = content.ageRating,
                                videoURL = ep.videoUrl,
                                type = ContentType.Series
                            )
                            viewModel.markAsWatched(content)
                            contentToPlay = c
                        },
                        onPlayMovie = { c ->
                            viewModel.markAsWatched(c)
                            contentToPlay = c
                        }
                    )
                }
            }
        }
    }

    contentToPlay?.let { content ->
        VideoPlayerScreen(
            content = content,
            onClose = { contentToPlay = null }
        )
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}