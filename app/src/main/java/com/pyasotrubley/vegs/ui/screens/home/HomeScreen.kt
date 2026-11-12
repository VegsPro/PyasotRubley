package com.pyasotrubley.vegs.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyasotrubley.vegs.data.AppInfo
import com.pyasotrubley.vegs.ui.components.AppIcon
import com.pyasotrubley.vegs.ui.components.GlassSurface
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 2 })

    var isEditMode by remember { mutableStateOf(false) }
    var selectedApp by remember { mutableStateOf<AppInfo?>(null) }
    var depthEffect by remember { mutableStateOf(false) }

    val timeFormat = SimpleDateFormat("H:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("EEE, d MMM", Locale("ru"))
    val now = Date()
    val timeStr = timeFormat.format(now)
    val dateStr = dateFormat.format(now).replaceFirstChar { it.uppercase() }

    val contentBlur = if (isEditMode) 12.dp else 0.dp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0B1220), Color(0xFF0F172A), Color(0xFF0B1220))
                )
            )
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (!isEditMode && settings.swipeUpDrawer && dragAmount < -40) {
                        viewModel.openDrawer()
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(if (isEditMode) Modifier.blur(contentBlur) else Modifier)
        ) {
            // Top clock (only home page, not edit)
            if (pagerState.currentPage == 1 && !isEditMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = timeStr,
                            color = Color.White,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light,
                            letterSpacing = (-1).sp
                        )
                        Text(text = dateStr, color = TextSecondary, fontSize = 14.sp)
                    }
                    Row {
                        IconButton(onClick = { viewModel.openDrawer() }) {
                            Icon(Icons.Default.Search, "Поиск", tint = TextPrimary)
                        }
                        IconButton(onClick = onOpenSettings) {
                            Icon(Icons.Default.Settings, "Настройки", tint = TextPrimary)
                        }
                    }
                }
            } else if (!isEditMode) {
                Spacer(Modifier.height(12.dp))
            } else {
                // Edit mode top actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    EditTopAction(
                        icon = Icons.Default.Remove,
                        label = "Убрать",
                        onClick = {
                            selectedApp?.let { viewModel.removeFromHome(it) }
                            isEditMode = false
                            selectedApp = null
                        }
                    )
                    EditTopAction(
                        icon = Icons.Default.Delete,
                        label = "Удалить",
                        onClick = {
                            // Uninstall would need special permission; for now just remove
                            selectedApp?.let { viewModel.removeFromHome(it) }
                            isEditMode = false
                            selectedApp = null
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = !isEditMode,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> ZeroScreen()
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(settings.homeColumns),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(homeApps, key = { it.uniqueId }) { app ->
                                val isSelected = isEditMode && selectedApp?.uniqueId == app.uniqueId
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.scale(if (isSelected) 1.15f else 1f)
                                ) {
                                    AppIcon(
                                        app = app,
                                        iconSize = settings.iconSizeDp.dp,
                                        showLabel = settings.showLabels && !isEditMode,
                                        labelSizeSp = settings.labelSizeSp,
                                        cornerRadius = settings.iconCornerRadius,
                                        onClick = {
                                            if (isEditMode) {
                                                selectedApp = app
                                            } else {
                                                viewModel.launch(app)
                                            }
                                        },
                                        onLongClick = {
                                            selectedApp = app
                                            isEditMode = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Page indicator
            if (settings.pageIndicator && !isEditMode) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(2) { i ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 3.dp)
                                .size(if (pagerState.currentPage == i) 8.dp else 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(
                                    if (pagerState.currentPage == i) AccentCyan
                                    else TextSecondary.copy(alpha = 0.4f)
                                )
                        )
                    }
                }
            }

            // Dock
            if (pagerState.currentPage == 1 && dockApps.isNotEmpty() && !isEditMode) {
                GlassSurface(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        dockApps.take(settings.dockColumns).forEach { app ->
                            AppIcon(
                                app = app,
                                iconSize = (settings.iconSizeDp * 0.95f).dp,
                                showLabel = false,
                                cornerRadius = settings.iconCornerRadius,
                                onClick = { viewModel.launch(app) },
                                onLongClick = {
                                    selectedApp = app
                                    isEditMode = true
                                }
                            )
                        }
                        IconButton(
                            onClick = { viewModel.openDrawer() },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                Icons.Default.Apps,
                                "Все приложения",
                                tint = AccentCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // ========== EDIT MODE OVERLAY ==========
        AnimatedVisibility(
            visible = isEditMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.35f))
            )
        }

        // Selected icon floating card in edit mode
        AnimatedVisibility(
            visible = isEditMode && selectedApp != null,
            enter = fadeIn() + slideInVertically { it / 4 },
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(32.dp)
                ) {
                    selectedApp?.let { app ->
                        AppIcon(
                            app = app,
                            iconSize = 72.dp,
                            showLabel = true,
                            labelSizeSp = 14,
                            cornerRadius = 18,
                            onClick = {},
                            onLongClick = null
                        )
                    }
                }
            }
        }

        // Bottom edit bar (like in the video)
        AnimatedVisibility(
            visible = isEditMode,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Depth effect button
                    TextButton(
                        onClick = { depthEffect = !depthEffect },
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Layers,
                            contentDescription = null,
                            tint = if (depthEffect) AccentCyan else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Эффект глубины",
                            color = if (depthEffect) AccentCyan else TextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // Bottom actions row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xE61A2332))
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        EditBottomAction(Icons.Default.Image, "Обои") {
                            // Placeholder — open wallpaper picker later
                            isEditMode = false
                        }
                        EditBottomAction(Icons.Default.Widgets, "Виджеты") {
                            isEditMode = false
                        }
                        EditBottomAction(Icons.Default.GridView, "Значки") {
                            isEditMode = false
                        }
                        EditBottomAction(Icons.Default.Settings, "Настройки") {
                            isEditMode = false
                            onOpenSettings()
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Tap outside to exit
                    TextButton(onClick = {
                        isEditMode = false
                        selectedApp = null
                    }) {
                        Text("Готово", color = AccentCyan, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun EditTopAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = label, tint = TextPrimary, modifier = Modifier.size(28.dp))
        }
        Text(label, color = TextPrimary, fontSize = 12.sp)
    }
}

@Composable
private fun EditBottomAction(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = label, tint = TextPrimary, modifier = Modifier.size(26.dp))
        }
        Text(label, color = TextSecondary, fontSize = 11.sp)
    }
}
