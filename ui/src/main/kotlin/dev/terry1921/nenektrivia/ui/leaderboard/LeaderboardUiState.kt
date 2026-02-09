package dev.terry1921.nenektrivia.ui.leaderboard

import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore

data class LeaderboardUiState(
    val isLoading: Boolean = true,
    val players: List<PlayerScore> = emptyList(),
    val error: String? = null
)
