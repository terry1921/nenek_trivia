package dev.terry1921.nenektrivia.ui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.dp

@Immutable
data class ElevationTokens(
    val level0: Int = 0,
    val level1: Int = 1,
    val level2: Int = 3,
    val level3: Int = 6,
    val level4: Int = 8,
    val level5: Int = 12
) {
    val e0 get() = level0.dp
    val e1 get() = level1.dp
    val e2 get() = level2.dp
    val e3 get() = level3.dp
    val e4 get() = level4.dp
    val e5 get() = level5.dp
}

val DefaultElevation = ElevationTokens()
