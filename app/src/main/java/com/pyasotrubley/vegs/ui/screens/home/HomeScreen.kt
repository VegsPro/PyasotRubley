package com.pyasotrubley.vegs.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyasotrubley.vegs.data.AppInfo
import com.pyasotrubley.vegs.data.LauncherSettings
import com.pyasotrubley.vegs.ui.components.AppIcon
import com.pyasotrubley.vegs.ui.components.GlassSurface
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.BackgroundDark
import com.pyasotrubley.vegs.ui.theme.LocalHazeState
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel
import dev.chrisbanes.haze.hazeSource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: LauncherViewModel,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    val homeApps by viewModel.homeApps.collectAsState()
    val dockApps by viewModel.dockApps.collectAsState()
    val isDrawerOpen by viewModel.isDrawerOpen.collectAsState()
    val hazeState = LocalHazeState.current

    // Simple 1-page for now (can expand to multiple later)
    val pagerState = rememberPagerState(pageCount = { 1 })

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundDark, Color(0xFF0F172A), BackgroundDark)
                )
            )
            .hazeSource(state = hazeState)
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (settings.swipeUpDrawer && dragAmount < -40) {
                        viewModel.openDrawer()
                    }
                }
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PyasotRubley",
                    color = AccentCyan,
                    fontSize = 18.sp
                )
                Row {
                    IconButton(onClick = { viewModel.openDrawer() }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = TextPrimary)
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextPrimary)
                    }
                }
            }

            // Home grid
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(settings.homeColumns),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(homeApps, key = { it.uniqueId }) { app ->
                        AppIcon(
                            app = app,
                            iconSize = settings.iconSizeDp.dp,
                            showLabel = settings.showLabels,
                            labelSizeSp = settings.labelSizeSp,
                            cornerRadius = settings.iconCornerRadius,
                            onClick = { viewModel.launch(app) },
                            onLongClick = { /* TODO: context menu / remove from home */ }
                        )
                    }
                }
            }

            // Dock
            if (dockApps.isNotEmpty()) {
                GlassSurface(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        dockApps.take(settings.dockColumns).forEach { app ->
                            AppIcon(
                                app = app,
                                iconSize = (settings.iconSizeDp * 0.9f).dp,
                                showLabel = false,
                                cornerRadius = settings.iconCornerRadius,
                                onClick = { viewModel.launch(app) }
                            )
                        }
                        // Apps button
                        IconButton(onClick = { viewModel.openDrawer() }) {
                            Icon(
                                Icons.Default.Apps,
                                contentDescription = "Drawer",
                                tint = AccentCyan,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
