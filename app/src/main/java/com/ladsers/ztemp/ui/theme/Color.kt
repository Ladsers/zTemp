package com.ladsers.ztemp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Purple700 = Color(0xFF3700B3) // not used
val Red400 = Color(0xFFCF6679) // not used

val Teal = Color(0xFF33BBBE)
val Orange = Color(0xFFFF6600)
val DarkOrange = Color(0xFFD46F2B) // for update card
val AlmostBlack = Color(0xFF131313) // for disabled card

internal val wearColorPalette: Colors = Colors(
    primary = Teal,
    primaryVariant = Purple700,
    secondary = Orange,
    secondaryVariant = Teal,
    error = Red400,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black
)