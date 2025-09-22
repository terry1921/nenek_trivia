package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class SpacingTokens(
    val xs: Int = 4,
    val sm: Int = 8,
    val md: Int = 12,
    val lg: Int = 16,
    val xl: Int = 24,
    val xxl: Int = 32,
    val xxxl: Int = 40,
    val topBarHeight: Int = 56
) {
    val extraSmall get() = xs.dp
    val small get() = sm.dp
    val medium get() = md.dp
    val large get() = lg.dp
    val extraLarge get() = xl.dp
    val extraExtraLarge get() = xxl.dp
    val extraExtraExtraLarge get() = xxxl.dp
    val topAppBarHeight get() = topBarHeight.dp
}

val DefaultSpacing = SpacingTokens()
