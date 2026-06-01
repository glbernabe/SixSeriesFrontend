package org.six.series.ui.components.basic.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.model.genre.Genre


@Composable
fun GenreCard(
    genre: Genre,
    topColor: Color,
    onGenreClick: (String) -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            topColor,
            Color.Black.copy(alpha = 0.9f)
        )
    )

    // This is for the FontSize so it looks good
    var fontSize by remember { mutableStateOf(24.sp) } // Start size
    var readyToDraw by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = gradient)
            .clickable { onGenreClick(genre.name) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genre.name,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                // In case the text Overflows the Box
                if (textLayoutResult.hasVisualOverflow) {
                    // Reduces the fontsize
                    fontSize *= 0.9f
                } else {
                    // If it doesn't overflow then it will show up
                    readyToDraw = true
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .graphicsLayer {
                    alpha = if (readyToDraw) 1f else 0f
                }
        )
    }
}