package com.pyasotrubley.vegs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.pyasotrubley.vegs.ui.navigation.LauncherNavHost
import com.pyasotrubley.vegs.ui.theme.PyasotRubleyTheme
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel
import dev.chrisbanes.haze.HazeState
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        setContent {
            val settings by viewModel.settings.collectAsState()
            val hazeState = remember { HazeState() }

            PyasotRubleyTheme(
                darkTheme = settings.darkTheme,
                glassEnabled = settings.glassEnabled,
                hazeState = hazeState
            ) {
                LauncherNavHost(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshApps()
    }
}
