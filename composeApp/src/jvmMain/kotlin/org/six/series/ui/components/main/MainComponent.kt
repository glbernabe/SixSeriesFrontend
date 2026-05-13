package org.six.series.ui.components.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.SingletonImageLoader
import coil3.compose.LocalPlatformContext
import org.koin.mp.KoinPlatform.getKoin
import org.six.series.ui.components.basic.AdaptiveTopBar
import org.six.series.ui.components.basic.HeroScreen
import org.six.series.ui.components.basic.genre.GenresGrid
import org.six.series.ui.components.viewmodels.MainPageViewModel

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
                    HeroScreen(state = viewModel.uiState)
                }

                composable(MainRoutes.Movies) {
                    PlaceholderScreen("Pantalla de Películas")
                }

                composable(MainRoutes.Series) {
                    PlaceholderScreen("Pantalla de Series")
                }

                composable(MainRoutes.Search) {
                    PlaceholderScreen("Buscador")
                }

                composable(MainRoutes.Genres) {
                    PlaceholderScreen("Explorar Géneros")
                }

                composable(MainRoutes.Profile) {
                    PlaceholderScreen("Perfil de Usuario")
                }
            }
        }
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
        // This is an example of the category
        if (text == "Explorar Géneros"){
            GenresGrid()
            return
        }

        Text(text = text, color = Color.White, style = MaterialTheme.typography.headlineMedium)



    }
}