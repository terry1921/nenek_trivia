package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class SpacingTokens(
    val xs: Int = 4, // 4dp
    val sm: Int = 8,
    val md: Int = 12,
    val lg: Int = 16,
    val xl: Int = 24,
    val xxl: Int = 32
) {
    val xsDp get() = xs.dp
    val smDp get() = sm.dp
    val mdDp get() = md.dp
    val lgDp get() = lg.dp
    val xlDp get() = xl.dp
    val xxlDp get() = xxl.dp
}

val DefaultSpacing = SpacingTokens()
