package org.six.series

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

// --- 1. PREDEFINED PROFILE COLORS ---
val ProfilePink = Color(0xFFE2A9F1)
val ProfileRed = Color(0xFFFF3131)
val ProfileBlue = Color(0xFF004AAD)
val ProfileGray = Color(0xFF6A6A69)
val ProfilePurple = Color(0xFFCE16FF)
val ProfileBlack = Color(0xFF000000)
val ProfileYellow = Color(0xFFFFDE59)
val ProfileGreen = Color(0xFF7ED957)

// --- 2. CONTRAST LOGIC ---
/**
 * Determines whether the content (text/icons) on a background should be Black or White
 * based on the background's luminance level.
 */
fun Color.contrastingColor(): Color {
    // Luminance returns a value between 0.0 (darkest) and 1.0 (lightest)
    return if (this.luminance() > 0.5f) Color.Black else Color.White
}

// --- 3. DYNAMIC SCHEME GENERATOR ---
/**
 * Generates a full Material 3 ColorScheme based on a specific profile color.
 * This ensures the entire app (buttons, backgrounds, texts) remains cohesive.
 */
fun profileScheme(baseColor: Color): ColorScheme {
    val onPrimaryColor = baseColor.contrastingColor()

    return lightColorScheme(
        primary = baseColor,
        onPrimary = onPrimaryColor,

        // Subtle containers based on the profile color for background elements
        primaryContainer = baseColor.copy(alpha = 0.15f),
        onPrimaryContainer = baseColor,

        secondary = baseColor.copy(alpha = 0.7f),
        onSecondary = Color.White,

        // Background and surface colors
        background = Color(0xFFFDFDFD),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFDFDFD),
        onSurface = Color(0xFF1C1B1F),

        // Surface variant for elements like TextFields or Cards
        surfaceVariant = baseColor.copy(alpha = 0.05f),
        onSurfaceVariant = Color(0xFF49454F),

        // Standard Material 3 Error colors
        error = Color(0xFFB3261E),
        onError = Color.White
    )
}

// --- 4. THEME COMPOSABLE (ROOT WRAPPER) ---
/**
 * Main Theme wrapper that applies the dynamic color scheme to the application.
 * @param selectedProfileColor The color retrieved from the DataStore/Profile settings.
 */
@Composable
fun AppTheme(
    selectedProfileColor: Color,
    content: @Composable () -> Unit
) {
    // Re-generate the color palette whenever the selectedProfileColor changes
    val colors = profileScheme(selectedProfileColor)

    MaterialTheme(
        colorScheme = colors,
        // You can also link a Typography() object here if defined
        content = content
    )
}