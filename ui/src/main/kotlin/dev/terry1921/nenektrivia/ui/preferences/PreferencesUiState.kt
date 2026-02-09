package dev.terry1921.nenektrivia.ui.preferences

import dev.terry1921.nenektrivia.model.category.Theme
import dev.terry1921.nenektrivia.ui.BuildConfig

data class PreferencesUiState(
    val isLoading: Boolean = false,
    val isMusicEnabled: Boolean = true,
    val isHapticsEnabled: Boolean = true,
    val selectedTheme: Theme = Theme.SYSTEM,
    val appVersion: String = BuildConfig.VERSION,
    val error: String? = null,
    val showThemeDialog: Boolean = false
)
