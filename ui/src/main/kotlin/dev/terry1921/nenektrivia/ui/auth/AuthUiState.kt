package dev.terry1921.nenektrivia.ui.auth

data class AuthUiState(
    val isFacebookLoading: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val errorMessage: String? = null
)
