package org.six.series.ui.components.basic.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.six.series.model.content.Content

val gradientColors = listOf(
    Color.Black.copy(alpha = 0.8f),
    Color.Transparent
)

@Composable
fun CarouselMovies(
    content: List<Content>
) {
    val pagerState = rememberPagerState(pageCount = { content.size })
    val scope = rememberCoroutineScope()

    val itemTextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        shadow = Shadow(
            color = Color.Black.copy(alpha = 0.5f),
            offset = Offset(2f, 4f),
            blurRadius = 8f
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        CarouselNavigationButton(
            onClick = {
                scope.launch {
                    val prevPage = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else content.size - 1
                    pagerState.animateScrollToPage(prevPage)
                }
            },
            isLeft = true,
            rotation = 270f
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(3f),
            userScrollEnabled = true
        ) { page ->
            Row(modifier = Modifier.fillMaxSize()) {
                CarouselInfoPanel(
                    itemTextStyle = itemTextStyle,
                    modifier = Modifier.weight(1f),
                    content = content[page]
                )

                Box(modifier = Modifier.weight(2f)) {
                    CarouselPoster(
                        modifier = Modifier.fillMaxSize(),
                        content = content[page]
                    )

                    // The Gradint to emerge the image of the movie with the background
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(0.2f)
                            .background(
                                brush = Brush.horizontalGradient(
                                    0.0f to Color.Black.copy(alpha = 0.9f),
                                    0.1f to Color.Black.copy(alpha = 0.8f),
                                    1f to Color.Transparent
                                )
                            )
                    )
                }
            }
        }

        CarouselNavigationButton(
            onClick = {
                scope.launch {
                    val nextPage = (pagerState.currentPage + 1) % content.size
                    pagerState.animateScrollToPage(nextPage)
                }
            },
            isLeft = false,
            rotation = 90f,
        )
    }
}