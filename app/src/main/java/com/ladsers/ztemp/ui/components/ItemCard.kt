package com.ladsers.ztemp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.ladsers.ztemp.ui.theme.Teal

@Composable
fun ItemCard(
    title: String,
    content: String? = null,
    enabled: Boolean = false,
    action: () -> Unit = {}
) {
    TitleCard(
        title = { Text(title, fontSize = 18.sp, color = Color.White) },
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = MaterialTheme.colors.surface,
            endBackgroundColor = MaterialTheme.colors.surface
        ),
        onClick = action,
        enabled = enabled
    ) {
        content?.let { Text(it, color = Teal) }
    }
}