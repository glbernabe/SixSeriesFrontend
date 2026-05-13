package org.six.series.ui.components.basic.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.six.series.model.content.Content

@Composable
fun ContentCard(
    content: Content,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(440.dp)
            .height(580.dp)
            .padding(8.dp)
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.Center,
        )
        {
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
                    contentScale = ContentScale.Fit
                )
            }

            // Text container
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
                    .fillMaxWidth()
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = content.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis // If the title is to large is shows "..."
                )

                // Here it can be shown additional information
                Text(
                    text = content.type.toString(),
                    style =
                        TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 24.sp
                        ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }
        }
    }
}