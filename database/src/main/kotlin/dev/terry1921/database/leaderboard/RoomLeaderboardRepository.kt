package dev.terry1921.database.leaderboard

import dev.terry1921.database.dao.ScoreDao
import dev.terry1921.database.entity.Score
import dev.terry1921.database.relations.ScoreWithUser

class RoomLeaderboardRepository(
    private val scoreDao: ScoreDao,
) : LeaderboardRepository {
    override suspend fun insertScore(score: Score) = scoreDao.insert(score)

    override suspend fun getTopScores(limit: Int): List<ScoreWithUser> = scoreDao.topScores(limit)
}
