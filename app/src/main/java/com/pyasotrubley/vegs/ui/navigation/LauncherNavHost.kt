package com.pyasotrubley.vegs.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pyasotrubley.vegs.ui.screens.drawer.DrawerScreen
import com.pyasotrubley.vegs.ui.screens.home.HomeScreen
import com.pyasotrubley.vegs.ui.screens.settings.SettingsScreen
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel

object Routes {
    const val HOME = "home"
    const val SETTINGS = "settings"
}

@Composable
fun LauncherNavHost(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            // Home + overlay Drawer
            androidx.compose.foundation.layout.Box(modifier = Modifier) {
                HomeScreen(
                    viewModel = viewModel,
                    onOpenSettings = { navController.navigate(Routes.SETTINGS) }
                )
                DrawerScreen(viewModel = viewModel)
            }
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
