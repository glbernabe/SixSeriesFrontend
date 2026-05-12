package org.six.series.ui.components.basic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.six.series.model.content.Content

@Composable
fun ContentCard(
    content: Content,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(260.dp)
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
            ) {

                AsyncImage(
                    model = content.portraitURL?.ifBlank {
                        "https://via.placeholder.com/300x450?text=No+Image"
                    },
                    contentDescription = content.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Contenedor del Texto
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.titleMedium, // Usamos tipografía del tema
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // If the title is to large is shows "..."
                )

                // Here it can be shown additional information
                Text(
                    text = "Película",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}