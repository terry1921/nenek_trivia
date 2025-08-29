package dev.terry1921.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.terry1921.ui.tokens.ColorTokens
import dev.terry1921.ui.tokens.DarkColorTokens
import dev.terry1921.ui.tokens.DefaultElevation
import dev.terry1921.ui.tokens.DefaultMotion
import dev.terry1921.ui.tokens.DefaultShapeTokens
import dev.terry1921.ui.tokens.DefaultSpacing
import dev.terry1921.ui.tokens.DefaultTypographyTokens
import dev.terry1921.ui.tokens.ElevationTokens
import dev.terry1921.ui.tokens.LightColorTokens
import dev.terry1921.ui.tokens.LocalColorTokens
import dev.terry1921.ui.tokens.LocalElevationTokens
import dev.terry1921.ui.tokens.LocalMotionTokens
import dev.terry1921.ui.tokens.LocalShapeTokens
import dev.terry1921.ui.tokens.LocalSpacingTokens
import dev.terry1921.ui.tokens.LocalTypographyTokens
import dev.terry1921.ui.tokens.MotionTokens
import dev.terry1921.ui.tokens.ShapeTokens
import dev.terry1921.ui.tokens.SpacingTokens
import dev.terry1921.ui.tokens.TypographyTokens
import dev.terry1921.ui.tokens.asMaterialShapes
import dev.terry1921.ui.tokens.asMaterialTypography

@Composable
fun NenekTheme(
    darkTheme: Boolean,
    colorTokens: ColorTokens = if (darkTheme) DarkColorTokens else LightColorTokens,
    typographyTokens: TypographyTokens = DefaultTypographyTokens,
    shapeTokens: ShapeTokens = DefaultShapeTokens,
    spacingTokens: SpacingTokens = DefaultSpacing,
    elevationTokens: ElevationTokens = DefaultElevation,
    motionTokens: MotionTokens = DefaultMotion,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) colorTokens.asDarkScheme() else colorTokens.asLightScheme()

    // Borde a borde opcional
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? android.app.Activity)?.window
        if (window != null) {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalColorTokens provides colorTokens,
        LocalTypographyTokens provides typographyTokens,
        LocalShapeTokens provides shapeTokens,
        LocalSpacingTokens provides spacingTokens,
        LocalElevationTokens provides elevationTokens,
        LocalMotionTokens provides motionTokens
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typographyTokens.asMaterialTypography(),
            shapes = shapeTokens.asMaterialShapes(),
            content = content
        )
    }
}
