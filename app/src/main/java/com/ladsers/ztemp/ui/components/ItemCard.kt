package com.ladsers.ztemp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.ladsers.ztemp.ui.theme.Teal

/**
 * Universal card for displaying information.
 */
@Composable
fun ItemCard(
    title: String,
    content: String? = null,
    enabled: Boolean = false,
    onClickAction: () -> Unit = {}
) {
    TitleCard(
        title = { Text(text = title, fontSize = 18.sp, color = Color.White) },
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = MaterialTheme.colors.surface,
            endBackgroundColor = MaterialTheme.colors.surface
        ),
        onClick = onClickAction,
        enabled = enabled
    ) {
        content?.let { Text(text = it, color = Teal) }
    }
}