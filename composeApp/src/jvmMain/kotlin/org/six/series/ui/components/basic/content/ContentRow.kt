package org.six.series.ui.components.basic.content

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.model.content.Content

@Composable
fun ContentRow(
    title: String,
    movies: List<Content>,
    onMovieClick: (Content) -> Unit,
) {
    val state = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.1f))
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontSize = 32.sp
            ),
            modifier = Modifier
                .padding(start = 30.dp, bottom = 8.dp, top = 20.dp)
        )

        Column(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                state = state,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(movies) { movie ->
                    ContentCard(content = movie, onClick = { onMovieClick(movie) })
                }
            }

            HorizontalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 8.dp),
                adapter = rememberScrollbarAdapter(scrollState = state),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4.dp),
                    hoverColor = Color.White.copy(alpha = 0.5f),
                    unhoverColor = Color.White.copy(alpha = 0.2f),
                    hoverDurationMillis = 300
                )
            )
        }
    }
}