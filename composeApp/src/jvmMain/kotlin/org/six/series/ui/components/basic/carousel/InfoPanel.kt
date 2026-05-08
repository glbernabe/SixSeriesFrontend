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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.profileButtonColors

@Composable
fun CarouselInfoPanel(
    itemTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
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
                // TITLE IMAGE AREA (Magenta Border)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .border(width = 2.dp, color = Color.Magenta)
                ) {
                    // Movie Title Image
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
                                text = "Información de toda la película una sinopsis totalmente normal.",
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
                                text = " Pel/Ser |  +N  |  nH nM / n Capítulos",
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