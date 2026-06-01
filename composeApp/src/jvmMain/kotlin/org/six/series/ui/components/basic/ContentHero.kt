package org.six.series.ui.components.basic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.six.series.model.content.Content
import org.six.series.profileButtonColors
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.logo_sixSeries

@Composable
fun ContentHero(
    content: Content,
    onPlay: () -> Unit
) {
    val itemTextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        shadow = androidx.compose.ui.graphics.Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(2f, 4f),
            blurRadius = 8f
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp)
            .background(Color.Black),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Panel izquierdo — info
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.Black),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = content.logoURL,
                    contentDescription = content.title,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.weight(2f)) {
                    Box(
                        modifier = Modifier
                            .weight(1.6f)
                            .fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = content.description,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = "${content.type.name} | +${content.ageRating} | ${content.duration?.hour}h ${content.duration?.minute}m",
                            style = TextStyle(
                                fontSize = 26.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Justify,
                                letterSpacing = 0.15.sp
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(vertical = 30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        modifier = Modifier.height(60.dp),
                        onClick = onPlay,
                        colors = profileButtonColors()
                    ) {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = "Ver Ahora",
                            style = itemTextStyle
                        )
                    }
                }
            }
        }

        // Panel derecho — poster
        Box(modifier = Modifier.weight(2f)) {
            AsyncImage(
                model = content.coverURL,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color.LightGray),
                error = painterResource(Res.drawable.logo_sixSeries)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.2f)
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            0.0f to Color.Black.copy(alpha = 0.9f),
                            0.1f to Color.Black.copy(alpha = 0.8f),
                            1f to Color.Transparent
                        )
                    )
            )
        }
    }
}