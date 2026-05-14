package org.six.series.ui.components.basic.genre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.ui.components.basic.ErrorNotification
import org.six.series.ui.components.viewmodels.MainUiState

@Composable
fun GenresGrid(
    state: MainUiState,
) {

    val baseColors = listOf(
        Color(0xFF004D40),
        Color(0xFF3E2723),
        Color(0xFF01579B),
        Color(0xFF00332A),
        Color(0xFF310000),
        Color(0xFF4A148C)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = "Generos",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 44.sp
            ),
            color = Color.White,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        when (state) {
            is MainUiState.Success -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 220.dp),
                    contentPadding = PaddingValues(bottom = 62.dp),
                    verticalArrangement = Arrangement.spacedBy(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(64.dp)
                ) {
                    itemsIndexed(state.genres) { index, genre ->
                        val baseColor = baseColors[index % baseColors.size]

                        GenreCard(
                            genre = genre,
                            topColor = baseColor
                        )
                    }
                }
            }

            is MainUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is MainUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = Color.Red)
                }
            }
        }
    }
}