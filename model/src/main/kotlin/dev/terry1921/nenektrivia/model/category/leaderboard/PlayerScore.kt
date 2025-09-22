package dev.terry1921.nenektrivia.model.category.leaderboard

data class PlayerScore(
    val position: Int,
    val avatarUrl: String?,
    val displayName: String,
    val score: Int
)
