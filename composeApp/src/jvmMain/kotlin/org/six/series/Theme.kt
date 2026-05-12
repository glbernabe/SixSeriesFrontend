package org.six.series

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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
val GhostWhite = Color(0xFFFCFCFC)
fun Color.contrastingColor(): Color {
    // Luminance returns a value between 0.0 (darkest) and 1.0 (lightest)
    return if (this.luminance() > 0.5f) Color.Black else GhostWhite
}

// --- 3. DYNAMIC SCHEME GENERATOR ---
/**
 * Generates a dynamic ColorScheme based on a single base color.
 * Documentation:
 * - primary: Uses the base color for key UI elements like Buttons and active states.
 * - onPrimary: Uses the contrast logic to ensure text/icons are visible over the primary color.
 * - surfaceVariant: Used by Cards and TextFields; here it's kept neutral but can be tinted.
 */
fun profileScheme(baseColor: Color): ColorScheme {
    val onPrimaryColor = baseColor.contrastingColor()

    return lightColorScheme(
        primary = baseColor,
        onPrimary = onPrimaryColor,

        // Primary Container: Used for less prominent highlights
        primaryContainer = baseColor.copy(alpha = 0.15f),
        onPrimaryContainer = baseColor,

        // Secondary: Used for less prominent components (70% opacity of base)
        secondary = baseColor.copy(alpha = 0.7f),
        onSecondary = Color.White,

        // Main Backgrounds: Standard near-white for clean contrast
        background = Color(0xFFFDFDFD),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFDFDFD),
        onSurface = Color(0xFF1C1B1F),

        // Surface Variant: Default background for Cards and OutlinedTextFields
        // Note: Currently set to a neutral dark tint with 5% alpha
        surfaceVariant = Color(0xFF1C1B1F).copy(alpha = 0.05f),
        onSurfaceVariant = Color(0xFF49454F),

        // Error states
        error = Color(0xFFB3261E),
        onError = Color.White
    )
}

/**
 * Composable function to retrieve themed button colors.
 * Documentation:
 * - containerColor: Pulls 'primary' from the current theme (baseColor).
 * - contentColor: Pulls 'onPrimary' from the current theme (contrast color).
 * - disabled states: Uses 'onSurface' with Material standard alpha values (12% and 38%).
 */
@Composable
fun profileButtonColors(): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
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