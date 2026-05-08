package org.six.series.ui.components.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ListItemDefaults.contentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.six.series.ui.components.basic.carousel.CarouselInfoPanel
import org.six.series.ui.components.basic.carousel.CarouselNavigationButton
import org.six.series.ui.components.basic.carousel.CarouselPoster

@Composable
fun CarouselMovies() {
    val itemTextStyle = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = contentColor,
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

        // The modifiers are passed here because of the inheritance
        CarouselNavigationButton(onClick = { })

        CarouselInfoPanel(
            itemTextStyle = itemTextStyle,
            modifier = Modifier.weight(1f)
        )

        CarouselPoster(
            modifier = Modifier.weight(2f)
        )

        CarouselNavigationButton(onClick = { })
    }

}