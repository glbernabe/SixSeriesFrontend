package org.six.series.ui.components.basic.carousel


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.model.content.Content
import org.six.series.profileButtonColors

@Composable
fun CarouselInfoPanel(
    itemTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    content: Content
) {
    var fontSizeValue by remember(content.title) { mutableFloatStateOf(60f) }
    var readyToDraw by remember(content.title) { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(width = 1.dp, color = Color.Gray)
        ) {
            Column {
                // TITLE AREA (Magenta Border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(width = 2.dp, color = Color.Magenta),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = content.title,
                        softWrap = true,
                        maxLines = 3,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = fontSizeValue.sp // Usamos el valor dinámico
                        ),
                        modifier = Modifier
                            .padding(16.dp)
                            .drawWithContent {
                                if (readyToDraw) drawContent()
                            },
                        onTextLayout = { textLayoutResult ->
                            // This is for the title font, so he can be responsive
                            if (textLayoutResult.hasVisualOverflow || textLayoutResult.didOverflowHeight) {
                                // If the text overflows, then we reduce
                                fontSizeValue *= 0.95f
                            } else {
                                // If it has the perfect size, then is return and shown
                                readyToDraw = true
                            }
                        }
                    )
                }

                // DETAIL CONTENT (Green Border)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(width = 2.dp, color = Color.Green)
                        .padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.weight(2f)) {

                        // SYNOPSIS AREA
                        Box(
                            modifier = Modifier
                                .weight(1.6f)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onSurface),
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = content.description,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                )
                            )
                        }

                        // META DETAILS AREA (Cyan Border)
                        Box(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxWidth()
                                .border(width = 2.dp, color = Color.Cyan)
                                .background(MaterialTheme.colorScheme.onSurface),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "${content.type.name} | +${content.ageRating} | ${content.duration?.hour}h ${content.duration?.minute}m",
                                // Pel/Ser |  +N  |  nH nM / n Capítulos"
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    letterSpacing = 0.15.sp,
                                )
                            )
                        }
                    }

                    // WATCH BUTTON AREA (Red Border)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(width = 2.dp, color = Color.Red)
                            .background(MaterialTheme.colorScheme.onSurface)
                            .padding(vertical = 30.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Button(
                            modifier = Modifier.height(60.dp),
                            onClick = {},
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
        }
    }
}