package org.six.series.ui.components.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.six.series.model.content.Content
import org.six.series.ui.components.basic.carousel.CarouselMovies
import org.six.series.ui.components.basic.content.MovieRow
import org.six.series.ui.components.viewmodels.MainUiState
import org.six.series.model.content.ContentType

@Composable
fun PrincipalScreen(state: MainUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        when (state) {
            is MainUiState.Loading -> {
                item { LinearProgressIndicator(modifier = Modifier.fillMaxWidth()) }
            }

            is MainUiState.Success -> {
                val moviesWithLogo = state.movies.filter { !it.logoURL.isNullOrBlank() }

                val cinema = state.movies.filter { it.type == ContentType.Movie }.shuffled()
                val series = state.movies.filter { it.type == ContentType.Series }.shuffled()
                val documentaries = state.movies.filter { it.type == ContentType.Documentary }.shuffled()

                val carousel = if (moviesWithLogo.size >= 5) {
                    moviesWithLogo.shuffled().take(5)
                } else {
                    moviesWithLogo.shuffled()
                }

                item {
                    Box(modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)) {
                        CarouselMovies(content = carousel)
                    }
                }

                item {
                    MovieRow(title = "Películas", movies = cinema) { /* Navigate */ }
                }

                item {
                    MovieRow(title = "Series", movies = series) { /* Navigate */ }
                }

                item {
                    MovieRow(title = "Documentales", movies = documentaries) { /* Navigate */ }
                }
            }

            is MainUiState.Error -> {
                item { ErrorNotification("Error al cargar contenido") { } }
            }
        }
    }
}