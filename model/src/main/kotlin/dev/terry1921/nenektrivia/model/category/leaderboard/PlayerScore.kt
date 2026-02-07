package dev.terry1921.nenektrivia.model.category.leaderboard

data class PlayerScore(
    val id: String = "",
    val image: String? = null,
    val points: Int = 0,
    val username: String = ""
)
