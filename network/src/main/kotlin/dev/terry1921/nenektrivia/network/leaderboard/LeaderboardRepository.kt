package dev.terry1921.nenektrivia.network.leaderboard

import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore

interface LeaderboardRepository {
    suspend fun fetchLeaderboard(forceRefresh: Boolean = false): List<PlayerScore>
}
