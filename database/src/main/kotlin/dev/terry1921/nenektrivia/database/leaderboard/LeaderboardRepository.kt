package dev.terry1921.nenektrivia.database.leaderboard

import dev.terry1921.nenektrivia.database.entity.Score
import dev.terry1921.nenektrivia.database.relations.ScoreWithUser

interface LeaderboardRepository {
    suspend fun insertScore(score: Score)

    suspend fun getTopScores(limit: Int): List<ScoreWithUser>
}
