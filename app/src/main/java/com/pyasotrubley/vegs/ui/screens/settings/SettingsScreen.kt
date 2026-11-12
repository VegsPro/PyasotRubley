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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyasotrubley.vegs.BuildConfig
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.BackgroundDark
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary
import com.pyasotrubley.vegs.viewmodel.LauncherViewModel

@Composable
fun SettingsScreen(
    viewModel: LauncherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад", tint = TextPrimary)
            }
            Text(
                text = "Настройки лаунчера",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(Modifier.height(20.dp))

        // Внешний вид
        Section("Внешний вид")
        SimpleCard {
            ToggleRow(
                title = "Glass эффект",
                subtitle = "Полупрозрачные карточки",
                checked = settings.glassEnabled,
                onChange = { enabled -> viewModel.updateSettings { it.copy(glassEnabled = enabled) } }
            )
            Spacer(Modifier.height(16.dp))
            ToggleRow(
                title = "Темная тема",
                subtitle = "Всегда темный фон",
                checked = settings.darkTheme,
                onChange = { enabled -> viewModel.updateSettings { it.copy(darkTheme = enabled) } }
            )
        }

        Spacer(Modifier.height(20.dp))

        // Сетка
        Section("Сетка и иконки")
        SimpleCard {
            SliderRow(
                title = "Колонок на столе",
                value = settings.homeColumns.toFloat(),
                range = 3f..6f,
                steps = 2,
                label = "${settings.homeColumns}",
                onChange = { v -> viewModel.updateSettings { it.copy(homeColumns = v.toInt().coerceIn(3, 6)) } }
            )
            Spacer(Modifier.height(18.dp))
            SliderRow(
                title = "Размер иконок",
                value = settings.iconSizeDp.toFloat(),
                range = 40f..80f,
                steps = 7,
                label = "${settings.iconSizeDp} dp",
                onChange = { v -> viewModel.updateSettings { it.copy(iconSizeDp = v.toInt().coerceIn(40, 80)) } }
            )
            Spacer(Modifier.height(18.dp))
            SliderRow(
                title = "Скругление",
                value = settings.iconCornerRadius.toFloat(),
                range = 4f..28f,
                steps = 5,
                label = "${settings.iconCornerRadius} dp",
                onChange = { v -> viewModel.updateSettings { it.copy(iconCornerRadius = v.toInt().coerceIn(4, 28)) } }
            )
            Spacer(Modifier.height(18.dp))
            ToggleRow(
                title = "Показывать названия",
                subtitle = null,
                checked = settings.showLabels,
                onChange = { enabled -> viewModel.updateSettings { it.copy(showLabels = enabled) } }
            )
            if (settings.showLabels) {
                Spacer(Modifier.height(12.dp))
                SliderRow(
                    title = "Размер текста",
                    value = settings.labelSizeSp.toFloat(),
                    range = 10f..16f,
                    steps = 5,
                    label = "${settings.labelSizeSp} sp",
                    onChange = { v -> viewModel.updateSettings { it.copy(labelSizeSp = v.toInt().coerceIn(10, 16)) } }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Док
        Section("Док")
        SimpleCard {
            SliderRow(
                title = "Приложений в доке",
                value = settings.dockColumns.toFloat(),
                range = 3f..6f,
                steps = 2,
                label = "${settings.dockColumns}",
                onChange = { v -> viewModel.updateSettings { it.copy(dockColumns = v.toInt().coerceIn(3, 6)) } }
            )
        }

        Spacer(Modifier.height(20.dp))

        // Поведение
        Section("Поведение")
        SimpleCard {
            ToggleRow(
                title = "Поиск в drawer",
                subtitle = null,
                checked = settings.searchInDrawer,
                onChange = { enabled -> viewModel.updateSettings { it.copy(searchInDrawer = enabled) } }
            )
            Spacer(Modifier.height(14.dp))
            ToggleRow(
                title = "Скрывать системные",
                subtitle = null,
                checked = settings.hideSystemApps,
                onChange = { enabled -> viewModel.updateSettings { it.copy(hideSystemApps = enabled) } }
            )
            Spacer(Modifier.height(14.dp))
            ToggleRow(
                title = "Свайп вверх = drawer",
                subtitle = null,
                checked = settings.swipeUpDrawer,
                onChange = { enabled -> viewModel.updateSettings { it.copy(swipeUpDrawer = enabled) } }
            )
            Spacer(Modifier.height(14.dp))
            ToggleRow(
                title = "Индикатор страниц",
                subtitle = null,
                checked = settings.pageIndicator,
                onChange = { enabled -> viewModel.updateSettings { it.copy(pageIndicator = enabled) } }
            )
        }

        Spacer(Modifier.height(20.dp))

        // Система
        Section("Система")
        SimpleCard {
            TextButton(
                onClick = {
                    context.startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Home, null, tint = AccentCyan)
                Spacer(Modifier.padding(8.dp))
                Text(
                    "Установить как лаунчер по умолчанию",
                    color = AccentCyan,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // О лаунчере
        Section("О лаунчере")
        SimpleCard {
            Text(
                "PyasotRubley Launcher",
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Версия ${BuildConfig.VERSION_NAME}",
                color = TextSecondary,
                fontSize = 13.sp,
                fontFamily = FontFamily.SansSerif
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Лаунчер с кастомизацией.\nРазработано для Vegs / MSTRBLOCK.",
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun Section(title: String) {
    Text(
        text = title,
        color = AccentCyan,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = FontFamily.SansSerif,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun SimpleCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2332)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String?,
    checked: Boolean,
    onChange: (Boolean) -> Unit
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
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
            if (subtitle != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = AccentCyan,
                uncheckedThumbColor = Color.White.copy(0.8f),
                uncheckedTrackColor = Color.White.copy(0.2f)
            )
        )
    }
}

@Composable
private fun SliderRow(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    steps: Int,
    label: String,
    onChange: (Float) -> Unit
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
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = label,
                color = AccentCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
        }
        Spacer(Modifier.height(8.dp))
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = range,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor = AccentCyan,
                activeTrackColor = AccentCyan,
                inactiveTrackColor = TextSecondary.copy(0.3f)
            )
        )
    }
}
