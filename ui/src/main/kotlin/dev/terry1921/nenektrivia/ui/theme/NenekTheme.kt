package dev.terry1921.nenektrivia.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import dev.terry1921.nenektrivia.ui.tokens.ColorTokens
import dev.terry1921.nenektrivia.ui.tokens.DarkColorTokens
import dev.terry1921.nenektrivia.ui.tokens.DefaultElevation
import dev.terry1921.nenektrivia.ui.tokens.DefaultMotion
import dev.terry1921.nenektrivia.ui.tokens.DefaultShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.DefaultSpacing
import dev.terry1921.nenektrivia.ui.tokens.DefaultTypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.ElevationTokens
import dev.terry1921.nenektrivia.ui.tokens.LightColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalColorTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalElevationTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalMotionTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalSpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.LocalTypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.MotionTokens
import dev.terry1921.nenektrivia.ui.tokens.ShapeTokens
import dev.terry1921.nenektrivia.ui.tokens.SpacingTokens
import dev.terry1921.nenektrivia.ui.tokens.TypographyTokens
import dev.terry1921.nenektrivia.ui.tokens.asMaterialShapes
import dev.terry1921.nenektrivia.ui.tokens.asMaterialTypography

@Composable
fun NenekTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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
        val window = (view.context as? Activity)?.window
        if (window != null) {
            // Edge-to-edge
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // Transparent system bars so the hero/gradients of AuthScreen respiran
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            // Icon/label contrast depending on theme
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = !darkTheme
            controller.isAppearanceLightNavigationBars = !darkTheme
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
