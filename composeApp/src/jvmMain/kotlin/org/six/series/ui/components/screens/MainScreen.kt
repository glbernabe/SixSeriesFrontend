package org.six.series.ui.components.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import org.six.series.ui.components.main.MainComponent

@Composable
fun MainScreen(navController: NavController) {
    MainComponent(rootNavController = navController)
}