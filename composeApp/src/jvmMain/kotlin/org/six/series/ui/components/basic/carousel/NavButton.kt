package org.six.series.ui.components.basic.carousel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.painterResource
import sixseries.composeapp.generated.resources.Res
import sixseries.composeapp.generated.resources.ic_arrow_up


@Composable
fun CarouselNavigationButton(
    onClick: () -> Unit,
    rotation: Float,
    isLeft: Boolean
) {
    val gradientColors = if (isLeft) {
        listOf(
            Color.Black,
            Color.Black
        )
    } else {
        listOf(
            Color.Transparent,
            Color.Black.copy(alpha = 0.8f)
        )
    }

    Column(
        modifier = Modifier
            .width(100.dp)
            .fillMaxHeight()
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() }
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IconArrow(rotation)
        }
    }
}

@Composable
fun IconArrow(rotationValue: Float) {
    Icon(
        painter = painterResource(Res.drawable.ic_arrow_up),
        contentDescription = "Right",
        modifier = Modifier
            .rotate(rotationValue)
            .padding(4.dp)
            .size(30.dp)
            .pointerHoverIcon(PointerIcon.Hand),
        tint = MaterialTheme.colors.onPrimary
    )
}

