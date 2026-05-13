package org.six.series.ui.components.basic.genre

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.model.genre.Genre

@Composable
fun GenresGrid() {
    val genres = listOf(
        Genre(1, "Acción"),
        Genre(2, "Aventuras"),
        Genre(3, "Sci fi"),
        Genre(4, "Documental"),
        Genre(5, "Deporte"),
        Genre(6, "Drama"),
        Genre(7, "Comedia"),
        Genre(8, "Terror"),
        Genre(9, "Ciencia Ficción"),
        Genre(10, "Documentales de Naturaleza") // Always have one Genre with a Long Text
    )

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

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 220.dp),
            contentPadding = PaddingValues(bottom = 62.dp),
            verticalArrangement = Arrangement.spacedBy(50.dp),
            horizontalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            itemsIndexed(genres) { index, genre ->
                val baseColor = baseColors[index % baseColors.size]

                GenreCard(
                    genre = genre,
                    topColor = baseColor
                )
            }
        }
    }
}