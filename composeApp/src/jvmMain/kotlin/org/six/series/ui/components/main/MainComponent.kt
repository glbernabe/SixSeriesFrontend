package org.six.series.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.mp.KoinPlatform.getKoin
import org.six.series.ui.components.basic.AdaptiveTopBar
import org.six.series.ui.components.basic.ErrorNotification
import org.six.series.ui.components.basic.MovieRow
import org.six.series.ui.components.basic.carousel.CarouselMovies
import org.six.series.ui.components.viewmodels.MainPageViewModel
import org.six.series.ui.components.viewmodels.MainUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent(rootNavController: NavController) {

    val internalNavController = rememberNavController()

    val context = LocalPlatformContext.current
    val imageLoader = SingletonImageLoader.get(context)

    val viewModel = remember {
        MainPageViewModel(
            context = context,
            imageLoader = imageLoader,
            getAllContentUseCase = getKoin().get()
        )
    }

    val adaptiveInfo = currentWindowAdaptiveInfo()

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

                    val state = viewModel.uiState

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF121212)),
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {

                        when (state) {

                            is MainUiState.Loading -> {

                                item {
                                    LinearProgressIndicator(
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            is MainUiState.Success -> {

                                item {
                                    CarouselMovies(content = state.movies)
                                }

                                item {
                                    MovieRow(
                                        title = "Tendencias ahora",
                                        movies = state.movies
                                    ) {}
                                }

                                item {
                                    MovieRow(
                                        title = "Añadidos recientemente",
                                        movies = state.movies.reversed()
                                    ) {}
                                }
                            }

                            is MainUiState.Error -> {

                                item {
                                    ErrorNotification(
                                        "Error al cargar contenido"
                                    ) {}
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}