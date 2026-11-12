package com.pyasotrubley.vegs.ui.screens.drawer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyasotrubley.vegs.ui.components.AppIcon
import com.pyasotrubley.vegs.ui.components.GlassCard
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.BackgroundDark
import com.pyasotrubley.vegs.ui.theme.LocalHazeState
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel
import dev.chrisbanes.haze.hazeSource

@Composable
fun DrawerScreen(
    viewModel: LauncherViewModel,
    modifier: Modifier = Modifier
) {
    val isOpen by viewModel.isDrawerOpen.collectAsState()
    val apps by viewModel.filteredApps.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val hazeState = LocalHazeState.current

    AnimatedVisibility(
        visible = isOpen,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundDark.copy(alpha = 0.95f))
                .hazeSource(state = hazeState)
                .statusBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header + Search
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Приложения",
                        color = TextPrimary,
                        fontSize = 22.sp
                    )
                    IconButton(onClick = { viewModel.closeDrawer() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (settings.searchInDrawer) {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        cornerRadius = 16.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = TextSecondary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            BasicTextField(
                                value = query,
                                onValueChange = { viewModel.setSearchQuery(it) },
                                singleLine = true,
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = TextPrimary,
                                    fontSize = 16.sp
                                ),
                                cursorBrush = SolidColor(AccentCyan),
                                modifier = Modifier.weight(1f),
                                decorationBox = { inner ->
                                    if (query.isEmpty()) {
                                        Text("Поиск приложений…", color = TextSecondary, fontSize = 16.sp)
                                    }
                                    inner()
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Grid of apps
                if (apps.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Приложения не найдены", color = TextSecondary)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(settings.homeColumns),
                        contentPadding = PaddingValues(bottom = 32.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(apps, key = { it.uniqueId }) { app ->
                            AppIcon(
                                app = app,
                                iconSize = settings.iconSizeDp.dp,
                                showLabel = settings.showLabels,
                                labelSizeSp = settings.labelSizeSp,
                                cornerRadius = settings.iconCornerRadius,
                                onClick = { viewModel.launch(app) }
                            )
                        }
                    }
                }
            }
        }
    }
}
