package com.pyasotrubley.vegs.ui.screens.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = TextPrimary
                )
            }
            Text(
                text = "Настройки лаунчера",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === Внешний вид ===
        SectionTitle("Внешний вид")
        GlassCard {
            SettingToggle(
                title = "Glass-эффект (blur)",
                subtitle = "Полупрозрачные карточки с размытием",
                checked = settings.glassEnabled,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(glassEnabled = enabled) }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingToggle(
                title = "Тёмная тема",
                subtitle = "Всегда тёмный фон",
                checked = settings.darkTheme,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(darkTheme = enabled) }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === Сетка и иконки ===
        SectionTitle("Сетка и иконки")
        GlassCard {
            SettingSlider(
                title = "Колонок на рабочем столе",
                value = settings.homeColumns.toFloat(),
                valueRange = 3f..6f,
                steps = 2,
                valueLabel = "${settings.homeColumns}",
                onValueChange = { v ->
                    viewModel.updateSettings {
                        it.copy(homeColumns = v.toInt().coerceIn(3, 6))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingSlider(
                title = "Размер иконок",
                value = settings.iconSizeDp.toFloat(),
                valueRange = 40f..80f,
                steps = 7,
                valueLabel = "${settings.iconSizeDp} dp",
                onValueChange = { v ->
                    viewModel.updateSettings {
                        it.copy(iconSizeDp = v.toInt().coerceIn(40, 80))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingSlider(
                title = "Скругление иконок",
                value = settings.iconCornerRadius.toFloat(),
                valueRange = 4f..28f,
                steps = 5,
                valueLabel = "${settings.iconCornerRadius} dp",
                onValueChange = { v ->
                    viewModel.updateSettings {
                        it.copy(iconCornerRadius = v.toInt().coerceIn(4, 28))
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SettingToggle(
                title = "Показывать названия",
                subtitle = null,
                checked = settings.showLabels,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(showLabels = enabled) }
                }
            )
            if (settings.showLabels) {
                Spacer(modifier = Modifier.height(12.dp))
                SettingSlider(
                    title = "Размер текста названий",
                    value = settings.labelSizeSp.toFloat(),
                    valueRange = 10f..16f,
                    steps = 5,
                    valueLabel = "${settings.labelSizeSp} sp",
                    onValueChange = { v ->
                        viewModel.updateSettings {
                            it.copy(labelSizeSp = v.toInt().coerceIn(10, 16))
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === Док ===
        SectionTitle("Док")
        GlassCard {
            SettingSlider(
                title = "Приложений в доке",
                value = settings.dockColumns.toFloat(),
                valueRange = 3f..6f,
                steps = 2,
                valueLabel = "${settings.dockColumns}",
                onValueChange = { v ->
                    viewModel.updateSettings {
                        it.copy(dockColumns = v.toInt().coerceIn(3, 6))
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === Поведение ===
        SectionTitle("Поведение")
        GlassCard {
            SettingToggle(
                title = "Поиск в App Drawer",
                subtitle = null,
                checked = settings.searchInDrawer,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(searchInDrawer = enabled) }
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            SettingToggle(
                title = "Скрывать системные приложения",
                subtitle = null,
                checked = settings.hideSystemApps,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(hideSystemApps = enabled) }
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            SettingToggle(
                title = "Свайп вверх — открыть drawer",
                subtitle = null,
                checked = settings.swipeUpDrawer,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(swipeUpDrawer = enabled) }
                }
            )
            Spacer(modifier = Modifier.height(14.dp))
            SettingToggle(
                title = "Индикатор страниц",
                subtitle = null,
                checked = settings.pageIndicator,
                onCheckedChange = { enabled ->
                    viewModel.updateSettings { it.copy(pageIndicator = enabled) }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === Система ===
        SectionTitle("Система")
        GlassCard {
            TextButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Home,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    "Установить как лаунчер по умолчанию",
                    color = AccentCyan,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // === О лаунчере ===
        SectionTitle("О лаунчере")
        GlassCard {
            Text(
                "PyasotRubley Launcher",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Версия ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                color = TextSecondary,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Современный лаунчер с полной кастомизацией и Glass UI.\nРазработано для Vegs / MSTRBLOCK.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        color = AccentCyan,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun SettingToggle(
    title: String,
    subtitle: String?,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
        GlassSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingSlider(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    valueLabel: String,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = valueLabel,
                color = AccentCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = AccentCyan,
                activeTrackColor = AccentCyan,
                inactiveTrackColor = TextSecondary.copy(alpha = 0.3f)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
