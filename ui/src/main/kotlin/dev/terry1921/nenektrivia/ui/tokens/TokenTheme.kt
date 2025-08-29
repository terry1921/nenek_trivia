package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

// Locals
val LocalColorTokens = staticCompositionLocalOf<ColorTokens> { LightColorTokens }
val LocalTypographyTokens = staticCompositionLocalOf { DefaultTypographyTokens }
val LocalShapeTokens = staticCompositionLocalOf { DefaultShapeTokens }
val LocalSpacingTokens = staticCompositionLocalOf { DefaultSpacing }
val LocalElevationTokens = staticCompositionLocalOf { DefaultElevation }
val LocalMotionTokens = staticCompositionLocalOf { DefaultMotion }

// Punto de acceso canónico
object TokenTheme {
    val colors: ColorTokens
        @Composable @ReadOnlyComposable
        get() = LocalColorTokens.current
    val typography: TypographyTokens
        @Composable @ReadOnlyComposable
        get() = LocalTypographyTokens.current
    val shapes: ShapeTokens
        @Composable @ReadOnlyComposable
        get() = LocalShapeTokens.current
    val spacing: SpacingTokens
        @Composable @ReadOnlyComposable
        get() = LocalSpacingTokens.current
    val elevation: ElevationTokens
        @Composable @ReadOnlyComposable
        get() = LocalElevationTokens.current
    val motion: MotionTokens
        @Composable @ReadOnlyComposable
        get() = LocalMotionTokens.current
}
