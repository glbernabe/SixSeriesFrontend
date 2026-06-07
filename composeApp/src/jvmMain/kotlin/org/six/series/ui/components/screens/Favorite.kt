package org.six.series.ui.components.basic.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.model.content.Content

@Composable
fun FavoritesScreen(
    favorites: List<Content>,
    favoritesLoading: Boolean,
    recentlyWatched: List<Content>,
    recentlyWatchedLoading: Boolean,
    onItemClick: (Content) -> Unit,
    onRemoveFavorite: (Content) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
            .verticalScroll(rememberScrollState())
    ) {
        SectionHeader(
            icon = {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = "Seguir viendo"
        )

        when {
            recentlyWatchedLoading -> SectionLoader()
            recentlyWatched.isEmpty() -> SectionEmpty("Todavía no has visto nada")
            else -> ContentRow(
                title = "",
                movies = recentlyWatched,
                onMovieClick = onItemClick
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Me gusta ──
        SectionHeader(
            icon = {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFE53935),
                    modifier = Modifier.size(26.dp)
                )
            },
            title = "Me gusta"
        )

        when {
            favoritesLoading -> SectionLoader()
            favorites.isEmpty() -> SectionEmpty("Pulsa el corazón en cualquier título para guardarlo aquí")
            else -> ContentRow(
                title = "",
                movies = favorites,
                onMovieClick = onItemClick
            )
        }

        Spacer(Modifier.height(48.dp))
    }
}

@Composable
private fun SectionHeader(
    icon: @Composable () -> Unit,
    title: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 28.dp, top = 32.dp, bottom = 8.dp)
    ) {
        icon()
        Spacer(Modifier.width(10.dp))
        Text(
            text = title,
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
    HorizontalDivider(
        color = Color.White.copy(alpha = 0.08f),
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 28.dp)
    )
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun SectionLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun SectionEmpty(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.White.copy(alpha = 0.35f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}