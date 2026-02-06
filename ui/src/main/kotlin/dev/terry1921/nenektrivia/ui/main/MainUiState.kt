package dev.terry1921.nenektrivia.ui.main

data class MainUiState(
    val displayName: String? = null,
    val avatarUrl: String? = null,
    val isLoggingOut: Boolean = false,
    val errorMessage: String? = null
)
