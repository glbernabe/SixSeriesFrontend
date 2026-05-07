package org.six.series.model

import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

// Class to make the structure of the Icons

data class NavigationItem(
    val icon: DrawableResource? = null,
    val route: String,
    val label: String? = null,
    val showLabel: Boolean = true,
)
