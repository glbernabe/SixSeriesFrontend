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
import coil3.compose.AsyncImage
import org.six.series.model.content.Content
import org.six.series.profileButtonColors

@Composable
fun CarouselInfoPanel(
    itemTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    content: Content
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.onBackground),
        ) {
            Column {
                // TITLE AREA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.onBackground),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = content.logoURL,
                        contentDescription = content.title,
                        modifier = Modifier.fillMaxSize()

                    )
                }

                // DETAIL CONTENT
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.weight(2f)) {

                        // SYNOPSIS AREA
                        Box(
                            modifier = Modifier
                                .weight(1.6f)
                                .fillMaxWidth(),
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

                        // META DETAILS AREA
                        Box(
                            modifier = Modifier
                                .weight(0.4f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = "${content.type.name} | +${content.ageRating} | ${content.duration?.hour}h ${content.duration?.minute}m",
                                // Pel/Ser |  +N  |  nH nM / n Capítulos"
                                style = TextStyle(
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Justify,
                                    letterSpacing = 0.15.sp,
                                )
                            )
                        }
                    }

                    // WATCH BUTTON AREA
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
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