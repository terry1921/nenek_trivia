package dev.terry1921.nenektrivia.ui.profile

import dev.terry1921.nenektrivia.model.category.Category

data class ProfileUiState(
    val isLoading: Boolean = true,
    val displayName: String? = null,
    val email: String? = null,
    val avatarUrl: String? = null,
    val knowledge: Map<Category, Int> = emptyMap(), // 1..100
    val error: String? = null
)
