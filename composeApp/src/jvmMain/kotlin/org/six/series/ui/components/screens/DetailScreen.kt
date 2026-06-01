package org.six.series.ui.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.six.series.model.content.Content
import org.six.series.model.content.Episode
import org.six.series.ui.components.basic.ContentHero
import org.six.series.ui.components.basic.EpisodeRow
import org.six.series.ui.components.basic.ErrorNotification
import org.six.series.ui.components.viewmodels.DetailUiState
import org.six.series.ui.components.viewmodels.DetailViewModel

@Composable
fun DetailScreen(
    content: Content,
    viewModel: DetailViewModel,
    onPlayEpisode: (Episode) -> Unit,
    onPlayMovie: (Content) -> Unit
) {
    LaunchedEffect(content.id) { viewModel.load(content) }

    when (val s = viewModel.uiState) {
        is DetailUiState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
        is DetailUiState.Error -> ErrorNotification(s.message) {}
        is DetailUiState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF0D0D0D))
            ) {
                item {
                    ContentHero(
                        content = s.content,
                        onPlay = {
                            if (s.episodes.isEmpty()) onPlayMovie(s.content)
                            else onPlayEpisode(s.episodes.first())
                        }
                    )
                }

                if (s.episodes.isEmpty()) return@LazyColumn

                val seasons = s.episodes.groupBy { it.season }

                seasons.forEach { (season, eps) ->
                    item {
                        Text(
                            "Temporada $season",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1A1A1A))
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                    }
                    items(eps.sortedBy { it.episode }) { ep ->
                        EpisodeRow(episode = ep, onClick = { onPlayEpisode(ep) })
                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.06f),
                            thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}