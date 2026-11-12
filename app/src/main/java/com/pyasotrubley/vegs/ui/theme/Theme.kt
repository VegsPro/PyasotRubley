package com.pyasotrubley.vegs.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import dev.chrisbanes.haze.HazeState

// Black-Blue Glass palette
val BackgroundDark = Color(0xFF0B1220)
val SurfaceGlass = Color(0xCC121A2B)
val BorderGlass = Color(0x33FFFFFF)
val AccentCyan = Color(0xFF38BDF8)
val AccentBlue = Color(0xFF0EA5E9)
val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)

private val DarkColorScheme = darkColorScheme(
    primary = AccentCyan,
    onPrimary = Color.Black,
    secondary = AccentBlue,
    background = BackgroundDark,
    surface = SurfaceGlass,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    outline = BorderGlass
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    secondary = AccentCyan,
    background = Color(0xFFF8FAFC),
    surface = Color(0xE6FFFFFF),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    outline = Color(0x33000000)
)

val LocalHazeState = staticCompositionLocalOf { HazeState() }
val LocalGlassEnabled = staticCompositionLocalOf { true }

@Composable
fun PyasotRubleyTheme(
    darkTheme: Boolean = true,
    glassEnabled: Boolean = true,
    hazeState: HazeState = HazeState(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalHazeState provides hazeState,
        LocalGlassEnabled provides glassEnabled
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
