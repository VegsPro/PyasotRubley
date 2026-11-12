package com.pyasotrubley.vegs.ui.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.pyasotrubley.vegs.data.AppInfo
import com.pyasotrubley.vegs.ui.theme.TextPrimary
import com.pyasotrubley.vegs.ui.theme.TextSecondary

@Composable
fun AppIcon(
    app: AppInfo,
    iconSize: Dp = 56.dp,
    showLabel: Boolean = true,
    labelSizeSp: Int = 12,
    cornerRadius: Int = 16,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(iconSize)
                .clip(RoundedCornerShape(cornerRadius.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            val drawable: Drawable? = app.icon
            if (drawable != null) {
                val bitmap = try {
                    drawable.toBitmap(iconSize.value.toInt() * 2, iconSize.value.toInt() * 2)
                } catch (e: Exception) {
                    null
                }
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = app.label,
                        modifier = Modifier
                            .size(iconSize)
                            .clip(RoundedCornerShape(cornerRadius.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = app.label,
                color = TextPrimary,
                fontSize = labelSizeSp.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
