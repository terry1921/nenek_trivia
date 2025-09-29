package dev.terry1921.nenektrivia.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import dev.terry1921.nenektrivia.ui.tokens.ColorTokens
import dev.terry1921.nenektrivia.ui.tokens.NenekPalette

fun asLightScheme(): ColorScheme = lightColorScheme(
    primary = NenekPalette.Pink,
    onPrimary = NenekPalette.White,
    secondary = NenekPalette.Orange,
    onSecondary = NenekPalette.Neutral0,
    tertiary = NenekPalette.Red,
    onTertiary = NenekPalette.White,

    error = NenekPalette.ErrorRed,
    onError = NenekPalette.White,

    background = NenekPalette.BackgroundLight,
    onBackground = NenekPalette.Neutral20,

    surface = NenekPalette.Neutral99,
    onSurface = NenekPalette.Neutral20,

    surfaceVariant = NenekPalette.Neutral95,
    onSurfaceVariant = NenekPalette.Neutral20,

    outline = NenekPalette.SurfaceGray,

    primaryContainer = NenekPalette.Magenta,
    onPrimaryContainer = NenekPalette.White,
    secondaryContainer = NenekPalette.Yellow,
    onSecondaryContainer = NenekPalette.Neutral0
)

fun ColorTokens.asDarkScheme(): ColorScheme = darkColorScheme(
    primary = NenekPalette.Pink,
    onPrimary = NenekPalette.Neutral0,
    secondary = NenekPalette.Orange,
    onSecondary = NenekPalette.Neutral0,
    tertiary = NenekPalette.Pink,
    onTertiary = NenekPalette.Neutral0,

    error = NenekPalette.Red,
    onError = NenekPalette.White,

    background = NenekPalette.BackgroundDark,
    onBackground = NenekPalette.Neutral90,

    surface = NenekPalette.Neutral20,
    onSurface = NenekPalette.Neutral90,

    surfaceVariant = NenekPalette.Neutral10,
    onSurfaceVariant = NenekPalette.Neutral90,

    outline = NenekPalette.SurfaceGray,

    primaryContainer = NenekPalette.DarkRed,
    onPrimaryContainer = NenekPalette.White,
    secondaryContainer = NenekPalette.Brown,
    onSecondaryContainer = NenekPalette.White
)
