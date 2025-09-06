package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.ui.graphics.Color

// Paleta base (derivada del logo)
object NenekPalette {
    val Pink = Color(0xFFE91E63)
    val Magenta = Color(0xFFD500F9)
    val Orange = Color(0xFFFF9800)
    val Red = Color(0xFFC62828)
    val DarkRed = Color(0xFF8B0000)
    val Blue = Color(0xFF2196F3)

    val BackgroundLight = Color(0xFFFAFAFA)
    val BackgroundDark = Color(0xFF121212)
    val SurfaceGray = Color(0xFF9E9E9E)

    val Neutral0 = Color(0xFF000000)
    val Neutral10 = Color(0xFF1A1A1A)
    val Neutral20 = Color(0xFF2A2A2A)
    val Neutral90 = Color(0xFFEAEAEA)
    val Neutral95 = Color(0xFFF5F5F5)
    val Neutral99 = Color(0xFFFCFCFC)
    val White = Color(0xFFFFFFFF)
}

// Tokens de color por modo (pueden venir de JSON si luego quieres)
data class ColorTokens(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val outline: Color,
    val circular: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val link: Color
)

val LightColorTokens =
    ColorTokens(
        primary = NenekPalette.Pink,
        onPrimary = NenekPalette.White,
        primaryContainer = NenekPalette.Magenta,
        onPrimaryContainer = NenekPalette.White,
        secondary = NenekPalette.Orange,
        onSecondary = NenekPalette.Neutral0,
        secondaryContainer = Color(0xFFFFE0B2),
        onSecondaryContainer = NenekPalette.Neutral0,
        tertiary = NenekPalette.Red,
        onTertiary = NenekPalette.White,
        background = NenekPalette.Neutral99,
        onBackground = NenekPalette.Neutral20,
        surface = NenekPalette.Neutral99,
        onSurface = NenekPalette.Neutral20,
        surfaceVariant = NenekPalette.Neutral95,
        onSurfaceVariant = NenekPalette.Neutral20,
        outline = Color(0xFFBDBDBD),
        circular = NenekPalette.Orange,
        error = Color(0xFFB00020),
        onError = NenekPalette.White,
        errorContainer = Color(0xFFFCD8DF),
        link = NenekPalette.Blue
    )

val DarkColorTokens =
    ColorTokens(
        primary = NenekPalette.Pink,
        onPrimary = NenekPalette.White,
        primaryContainer = NenekPalette.DarkRed,
        onPrimaryContainer = NenekPalette.White,
        secondary = NenekPalette.Orange,
        onSecondary = NenekPalette.White,
        secondaryContainer = Color(0xFF6A3F00),
        onSecondaryContainer = NenekPalette.White,
        tertiary = NenekPalette.Red,
        onTertiary = NenekPalette.White,
        background = NenekPalette.Neutral10,
        onBackground = NenekPalette.Neutral90,
        surface = NenekPalette.Neutral10,
        onSurface = NenekPalette.Neutral90,
        surfaceVariant = NenekPalette.Neutral20,
        onSurfaceVariant = NenekPalette.Neutral90,
        outline = Color(0xFF555555),
        circular = NenekPalette.Orange,
        error = Color(0xFFCF6679),
        onError = NenekPalette.Neutral0,
        errorContainer = Color(0xFFB1384E),
        link = NenekPalette.Blue
    )
