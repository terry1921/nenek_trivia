package dev.terry1921.nenektrivia.domain.leaderboard

import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import dev.terry1921.nenektrivia.network.leaderboard.LeaderboardRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(private val repository: LeaderboardRepository) {
    suspend operator fun invoke(forceRefresh: Boolean = false): List<PlayerScore> =
        repository.fetchLeaderboard(forceRefresh)
}
