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
import com.pyasotrubley.vegs.ui.theme.LocalHazeState
import com.pyasotrubley.vegs.ui.theme.SurfaceGlass
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val glassEnabled = LocalGlassEnabled.current
    val hazeState = LocalHazeState.current
    val shape = RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .clip(shape)
            .then(
                if (glassEnabled) {
                    Modifier
                        .hazeEffect(
                            state = hazeState,
                            style = HazeMaterials.thin(SurfaceGlass)
                        ) {
                            blurRadius = 20.dp
                        }
                        .background(SurfaceGlass)
                        .border(1.dp, BorderGlass, shape)
                } else {
                    Modifier
                        .background(MaterialTheme.colorScheme.surface, shape)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), shape)
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
    val hazeState = LocalHazeState.current
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .then(
                if (glassEnabled) {
                    Modifier
                        .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin()) {
                            blurRadius = 16.dp
                        }
                        .background(SurfaceGlass.copy(alpha = 0.6f))
                        .border(0.5.dp, BorderGlass, shape)
                } else {
                    Modifier.background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f), shape)
                }
            ),
        content = content
    )
}
