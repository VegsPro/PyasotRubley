package com.pyasotrubley.vegs.ui.screens.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyasotrubley.vegs.BuildConfig
import com.pyasotrubley.vegs.ui.components.GlassCard
import com.pyasotrubley.vegs.ui.components.GlassSwitch
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.BackgroundDark
import com.pyasotrubley.vegs.ui.theme.LocalHazeState
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel
import dev.chrisbanes.haze.hazeSource
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color

@Composable
fun SettingsScreen(
    viewModel: LauncherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current
    val hazeState = LocalHazeState.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .hazeSource(state = hazeState)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Text(
                text = "Настройки лаунчера",
                color = TextPrimary,
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Glass
        SectionTitle("Внешний вид")
        GlassCard {
            SettingRow(
                title = "Glass-эффект (blur)",
                subtitle = "Полупрозрачные карточки с размытием"
            ) {
                GlassSwitch(
                    checked = settings.glassEnabled,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(glassEnabled = enabled) } }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            SettingRow(
                title = "Тёмная тема",
                subtitle = "Всегда тёмный фон"
            ) {
                GlassSwitch(
                    checked = settings.darkTheme,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(darkTheme = enabled) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid & Icons
        SectionTitle("Сетка и иконки")
        GlassCard {
            Text("Колонок на рабочем столе: ${settings.homeColumns}", color = TextPrimary, fontSize = 14.sp)
            Slider(
                value = settings.homeColumns.toFloat(),
                onValueChange = { v -> viewModel.updateSettings { it.copy(homeColumns = v.toInt().coerceIn(3, 6)) } },
                valueRange = 3f..6f,
                steps = 2,
                colors = SliderDefaults.colors(thumbColor = AccentCyan, activeTrackColor = AccentCyan)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Размер иконок: ${settings.iconSizeDp} dp", color = TextPrimary, fontSize = 14.sp)
            Slider(
                value = settings.iconSizeDp.toFloat(),
                onValueChange = { v -> viewModel.updateSettings { it.copy(iconSizeDp = v.toInt().coerceIn(40, 80)) } },
                valueRange = 40f..80f,
                colors = SliderDefaults.colors(thumbColor = AccentCyan, activeTrackColor = AccentCyan)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Скругление иконок: ${settings.iconCornerRadius} dp", color = TextPrimary, fontSize = 14.sp)
            Slider(
                value = settings.iconCornerRadius.toFloat(),
                onValueChange = { v -> viewModel.updateSettings { it.copy(iconCornerRadius = v.toInt().coerceIn(4, 28)) } },
                valueRange = 4f..28f,
                colors = SliderDefaults.colors(thumbColor = AccentCyan, activeTrackColor = AccentCyan)
            )
            Spacer(modifier = Modifier.height(8.dp))
            SettingRow("Показывать названия", null) {
                GlassSwitch(
                    checked = settings.showLabels,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(showLabels = enabled) } }
                )
            }
            if (settings.showLabels) {
                Text("Размер текста: ${settings.labelSizeSp} sp", color = TextPrimary, fontSize = 14.sp)
                Slider(
                    value = settings.labelSizeSp.toFloat(),
                    onValueChange = { v -> viewModel.updateSettings { it.copy(labelSizeSp = v.toInt().coerceIn(10, 16)) } },
                    valueRange = 10f..16f,
                    colors = SliderDefaults.colors(thumbColor = AccentCyan, activeTrackColor = AccentCyan)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dock
        SectionTitle("Док")
        GlassCard {
            Text("Приложений в доке: ${settings.dockColumns}", color = TextPrimary, fontSize = 14.sp)
            Slider(
                value = settings.dockColumns.toFloat(),
                onValueChange = { v -> viewModel.updateSettings { it.copy(dockColumns = v.toInt().coerceIn(3, 6)) } },
                valueRange = 3f..6f,
                steps = 2,
                colors = SliderDefaults.colors(thumbColor = AccentCyan, activeTrackColor = AccentCyan)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Behavior
        SectionTitle("Поведение")
        GlassCard {
            SettingRow("Поиск в App Drawer", null) {
                GlassSwitch(
                    checked = settings.searchInDrawer,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(searchInDrawer = enabled) } }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SettingRow("Скрывать системные приложения", null) {
                GlassSwitch(
                    checked = settings.hideSystemApps,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(hideSystemApps = enabled) } }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SettingRow("Свайп вверх — открыть drawer", null) {
                GlassSwitch(
                    checked = settings.swipeUpDrawer,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(swipeUpDrawer = enabled) } }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SettingRow("Индикатор страниц", null) {
                GlassSwitch(
                    checked = settings.pageIndicator,
                    onCheckedChange = { enabled -> viewModel.updateSettings { it.copy(pageIndicator = enabled) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // System
        SectionTitle("Система")
        GlassCard {
            TextButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Home, contentDescription = null, tint = AccentCyan)
                Spacer(modifier = Modifier.padding(8.dp))
                Text("Установить как лаунчер по умолчанию", color = AccentCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // About
        SectionTitle("О лаунчере")
        GlassCard {
            Text("PyasotRubley Launcher", color = TextPrimary, fontSize = 16.sp)
            Text("Версия ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})", color = TextSecondary, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Современный лаунчер с полной кастомизацией и Glass UI.\nРазработано для Vegs / MSTRBLOCK.",
                color = TextSecondary,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = AccentCyan,
        fontSize = 14.sp,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun SettingRow(
    title: String,
    subtitle: String?,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextPrimary, fontSize = 15.sp)
            if (subtitle != null) {
                Text(subtitle, color = TextSecondary, fontSize = 12.sp)
            }
        }
        trailing()
    }
}
