package com.example.studentmarketplaceapp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Pink = Color(0xFFFF485C)
val PinkLight = Color(0xFFBE8399)
val DarkBlue = Color(0xFF111747)
val DarkBlueLight = Color(0xFF73788E)
val Orange = Color(0xFFFA754A)
val Magenta = Color(0xFFEE4A89)
val MagentaLight = Color(0xFFFFE0ED)
val DarkBrown = Color(0xFF4E342E)
val LightGray = Color(0xFFBDBDBD)
val ButtonGray = Color(0xFF8D6E63)
val LightBackgroundGray = Color(0xFFE0E0E0)
val GreenCheck = Color(0xFF4CAF50)
val purpleGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF6750A4),  // Base Purple
        Color(0xFF6750A4),  // Lighter Purple
        Color(0xFF4527A0)   // Darker Purple
    )
)
val purple = Color(0xFF4527A0)

@Immutable
data class AppColors(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val secondarySurface: Color,
    val onSecondarySurface: Color,
    val regularSurface: Color,
    val onRegularSurface: Color,
    val actionSurface: Color,
    val onActionSurface: Color,
    val highlightSurface: Color,
    val onHighlightSurface: Color,
    val onBackgroundText: Color,
    val gradientBackground: Brush,
    val purple: Color
)

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        background = Color.Unspecified,
        onBackground = Color.Unspecified,
        surface = Color.Unspecified,
        onSurface = Color.Unspecified,
        secondarySurface = Color.Unspecified,
        onSecondarySurface = Color.Unspecified,
        regularSurface = Color.Unspecified,
        onRegularSurface = Color.Unspecified,
        actionSurface = Color.Unspecified,
        onActionSurface = Color.Unspecified,
        highlightSurface = Color.Unspecified,
        onHighlightSurface = Color.Unspecified,
        onBackgroundText = Color.Unspecified,
        gradientBackground = Brush.linearGradient(),
        purple = Color.Unspecified
    )
}

val extendedColors = AppColors(
    background = Color.White,
    onBackground = DarkBlue,
    surface = DarkBlue,
    onSurface = Color.White,
    secondarySurface = DarkBlue,
    onSecondarySurface = Color.White,
    regularSurface = Pink,
    onRegularSurface = Color.White,
    actionSurface = DarkBlue,
    onActionSurface = Orange,
    highlightSurface = Magenta,
    onHighlightSurface = MagentaLight,
    onBackgroundText = LightBackgroundGray,
    gradientBackground = purpleGradient,
    purple = purple
)