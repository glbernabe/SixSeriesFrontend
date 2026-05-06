package org.six.series

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

// ALL THE COLORS OF THE PROFILES
val ProfilePink = Color(0xFFE2A9F1)
val ProfileRed = Color(0xFFFF3131)
val ProfileBlue = Color(0xFF004AAD)
val ProfileGray = Color(0xFF6A6A69)
val ProfilePurple = Color(0xFFCE16FF)
val ProfileBlack = Color(0xFF000000)
val ProfileYellow = Color(0xFFFFDE59)
val ProfileGreen = Color(0xFF7ED957)

fun profileScheme(baseColor: Color): ColorScheme {
    return lightColorScheme(
        primary = baseColor,
        onPrimary = if (baseColor == Color.Black) Color.White else Color.Black, // Contrast ajustment
        secondary = baseColor.copy(alpha = 0.7f),
        onSecondary = Color.White,
        background = Color(0xFFFDFDFD),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFDFDFD),
        onSurface = Color(0xFF1C1B1F)
        // Add more if it's viable
    )
}
@Composable
fun AppTheme(
    selectedProfileColor: Color,
    content: @Composable () -> Unit
) {
    val colors = profileScheme(selectedProfileColor)

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}