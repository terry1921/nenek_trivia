package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

data class ShapeTokens(
    val radiusXs: Float,
    val radiusSm: Float,
    val radiusMd: Float,
    val radiusLg: Float,
    val radiusXl: Float
)

val DefaultShapeTokens =
    ShapeTokens(
        radiusXs = 4f,
        radiusSm = 8f,
        radiusMd = 12f,
        radiusLg = 16f,
        radiusXl = 24f
    )

fun ShapeTokens.asMaterialShapes() = Shapes(
    extraSmall = RoundedCornerShape(radiusXs.dp),
    small = RoundedCornerShape(radiusSm.dp),
    medium = RoundedCornerShape(radiusMd.dp),
    large = RoundedCornerShape(radiusLg.dp),
    extraLarge = RoundedCornerShape(radiusXl.dp)
)
