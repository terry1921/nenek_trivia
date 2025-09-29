package dev.terry1921.nenektrivia.model.category.preference

import dev.terry1921.nenektrivia.model.category.Theme

data class UserSettings(
    val isMusicEnabled: Boolean = true,
    val isHapticsEnabled: Boolean = true,
    val theme: Theme = Theme.SYSTEM
)
