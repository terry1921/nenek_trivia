package dev.terry1921.database.leaderboard

import dev.terry1921.database.entity.Score
import dev.terry1921.database.relations.ScoreWithUser

interface LeaderboardRepository {
    suspend fun insertScore(score: Score)

    suspend fun getTopScores(limit: Int): List<ScoreWithUser>
}
