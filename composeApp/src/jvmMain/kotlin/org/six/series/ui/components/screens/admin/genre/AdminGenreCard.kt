package org.six.series.ui.components.screens.admin.genre

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminGenreCard(
    genreName: String,
    topColor: Color,
    onGenreClick: (String) -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            topColor,
            Color.Black.copy(alpha = 0.9f)
        )
    )

    var fontSize by remember { mutableStateOf(24.sp) }
    var readyToDraw by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = gradient)
            .clickable { onGenreClick(genreName) }
            // Añadimos el puntero del sistema operativo para que sepa que es interactivo
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = genreName,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = fontSize,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    fontSize *= 0.9f
                } else {
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