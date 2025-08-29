package dev.terry1921.ui.tokens

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable

@Immutable
data class MotionTokens(
    val fast: Int = 120,
    val normal: Int = 200,
    val slow: Int = 300,
    val easingStandard: Easing = EaseInOut
) {
    fun tweenFast() = tween<Int>(durationMillis = fast, easing = easingStandard)

    fun tweenNormal() = tween<Int>(durationMillis = normal, easing = easingStandard)

    fun tweenSlow() = tween<Int>(durationMillis = slow, easing = easingStandard)
}

val DefaultMotion = MotionTokens()
