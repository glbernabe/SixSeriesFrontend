package org.six.series.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.six.series.ui.components.basic.AdaptiveTopBar
import org.six.series.ui.components.basic.ErrorNotification
import org.six.series.ui.components.basic.carousel.CarouselMovies
import org.six.series.ui.components.viewmodels.MainPageViewModel
import org.six.series.ui.components.viewmodels.MainUiState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent(rootNavController: NavController) {
    val internalNavController = rememberNavController()
    val viewModel: MainPageViewModel = koinViewModel()
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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 30.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        when (state) {
                            is MainUiState.Loading -> { LinearProgressIndicator() }
                            is MainUiState.Success -> {
                                CarouselMovies(
                                    content = state.movies
                                )
                            }
                        is MainUiState.Error -> {
                            ErrorNotification("Ha ocurrido un error al cargar.", {})
                        }
                        }
                    }
                }

                composable(MainRoutes.Movies) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: PELÍCULAS", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Series) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: SERIES", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Search) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: BÚSQUEDA", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Genres) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: GÉNEROS", color = Color.White, fontSize = 24.sp)
                    }
                }
                composable(MainRoutes.Profile) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("ESTÁS EN: AJUSTES DE PERFIL", color = Color.White, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}