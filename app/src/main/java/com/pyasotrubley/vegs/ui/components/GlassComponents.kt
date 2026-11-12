package com.pyasotrubley.vegs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.BorderGlass
import com.pyasotrubley.vegs.ui.theme.LocalGlassEnabled
import com.pyasotrubley.vegs.ui.theme.SurfaceGlass

/**
 * Lightweight card. Haze is expensive — we use simple translucent background
 * instead of real-time blur for performance.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val glassEnabled = LocalGlassEnabled.current
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (glassEnabled) {
                    Modifier
                        .background(SurfaceGlass)
                        .border(1.dp, BorderGlass, shape)
                } else {
                    Modifier
                        .background(Color(0xFF1A2332), shape)
                        .border(1.dp, Color(0x22FFFFFF), shape)
                }
            )
            .padding(16.dp),
        content = content
    )
}

@Composable
fun GlassSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = AccentCyan,
            uncheckedThumbColor = Color.White.copy(alpha = 0.8f),
            uncheckedTrackColor = Color.White.copy(alpha = 0.2f)
        )
    )
}

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val glassEnabled = LocalGlassEnabled.current
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .then(
                if (glassEnabled) {
                    Modifier
                        .background(SurfaceGlass.copy(alpha = 0.7f))
                        .border(0.5.dp, BorderGlass, shape)
                } else {
                    Modifier
                        .background(Color(0xE61A2332), shape)
                        .border(0.5.dp, Color(0x22FFFFFF), shape)
                }
            ),
        content = content
    )
}
