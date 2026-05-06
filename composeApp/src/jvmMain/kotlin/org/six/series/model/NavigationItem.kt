package org.six.series.model

import androidx.compose.ui.graphics.vector.ImageVector

// Class to make the structure of the Icons

data class NavigationItem(
    val icon: ImageVector? = null,
    val route: String,
    val label: String
)
