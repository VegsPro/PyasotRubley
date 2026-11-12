package com.pyasotrubley.vegs.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyasotrubley.vegs.ui.theme.AccentCyan
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary

@Composable
fun ZeroScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Zero Screen",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Лента и быстрый доступ",
            color = TextSecondary,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(20.dp))

        // Weather card
        FeedCard(
            icon = Icons.Default.WbSunny,
            title = "Погода",
            subtitle = "Сегодня · 1° · Ясно",
            accent = Color(0xFFFFB74D)
        )

        Spacer(Modifier.height(12.dp))

        // News placeholders
        FeedCard(
            icon = Icons.Default.Newspaper,
            title = "Новости",
            subtitle = "Тренды и важные события появятся здесь",
            accent = AccentCyan
        )

        Spacer(Modifier.height(12.dp))

        FeedCard(
            icon = Icons.Default.SportsEsports,
            title = "Игры",
            subtitle = "Рекомендации игр скоро",
            accent = Color(0xFF81C784)
        )

        Spacer(Modifier.height(12.dp))

        FeedCard(
            icon = Icons.Default.Cloud,
            title = "Discovery",
            subtitle = "Обои, темы и шрифты — в следующих обновлениях",
            accent = Color(0xFFBA68C8)
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Свайп вправо → рабочий стол",
            color = TextSecondary.copy(alpha = 0.6f),
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun FeedCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    accent: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A2332))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(accent.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.padding(12.dp))
            Column {
                Text(title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(subtitle, color = TextSecondary, fontSize = 13.sp)
            }
        }
    }
}
