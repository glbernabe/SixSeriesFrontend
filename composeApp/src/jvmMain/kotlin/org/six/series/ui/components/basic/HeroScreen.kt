package org.six.series.ui.components.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.six.series.model.content.Content
import org.six.series.model.content.ContentType
import org.six.series.ui.components.basic.carousel.CarouselMovies
import org.six.series.ui.components.basic.content.ContentRow
import org.six.series.ui.components.viewmodels.MainUiState

val gradientColors = listOf(
    Color.Black.copy(alpha = 0.8f),
    Color.Transparent
)

@Composable
fun HeroScreen(
    state: MainUiState,
    onPlayContent: (Content) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.radialGradient(colors = gradientColors)),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        when (state) {
            is MainUiState.Loading -> {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }

            is MainUiState.Success -> {
                val moviesWithLogo = state.movies.filter { !it.logoURL.isNullOrBlank() }

                val cinema       = state.movies.filter { it.type == ContentType.Movie }.shuffled()
                val series       = state.movies.filter { it.type == ContentType.Series }.shuffled()
                val documentaries = state.movies.filter { it.type == ContentType.Documentary }.shuffled()

                val carousel = if (moviesWithLogo.size >= 5) moviesWithLogo.shuffled().take(5)
                else moviesWithLogo.shuffled()

                item {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)) {
                        CarouselMovies(content = carousel, onPlayContent = onPlayContent)
                    }
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                            .fillMaxWidth()
                            .height(18.dp)
                    )
                }

                item {
                    ContentRow(title = "Películas",    movies = cinema,         onMovieClick = { onPlayContent(it) })
                }
                item {
                    ContentRow(title = "Series",       movies = series,         onMovieClick = { onPlayContent(it) })
                }
                item {
                    ContentRow(title = "Documentales", movies = documentaries,  onMovieClick = { onPlayContent(it) })
                }

            }

            is MainUiState.Error -> {
                item { ErrorNotification("Error al cargar contenido") { } }
            }

        }
    }
}