package com.pyasotrubley.vegs.data

data class LauncherSettings(
    val glassEnabled: Boolean = false,
    val homeColumns: Int = 4,
    val iconSizeDp: Int = 56,
    val showLabels: Boolean = true,
    val dockColumns: Int = 5,
    val labelSizeSp: Int = 12,
    val pageIndicator: Boolean = true,
    val darkTheme: Boolean = true,
    val blurStrength: Float = 20f,
    val iconCornerRadius: Int = 16,
    val searchInDrawer: Boolean = true,
    val hideSystemApps: Boolean = false,
    val doubleTapLock: Boolean = false,
    val swipeUpDrawer: Boolean = true
)
