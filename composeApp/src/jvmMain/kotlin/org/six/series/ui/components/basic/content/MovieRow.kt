package org.six.series.ui.components.basic.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.six.series.model.content.Content

@Composable
fun MovieRow(
    title: String,
    movies: List<Content>,
    onMovieClick: (Content) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                ContentCard(content = movie)
            }
        }
    }
}