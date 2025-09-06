package dev.terry1921.nenektrivia.database.leaderboard

import dev.terry1921.nenektrivia.database.dao.ScoreDao
import dev.terry1921.nenektrivia.database.entity.Score
import dev.terry1921.nenektrivia.database.relations.ScoreWithUser

class RoomLeaderboardRepository(private val scoreDao: ScoreDao) : LeaderboardRepository {
    override suspend fun insertScore(score: Score) = scoreDao.insert(score)

    override suspend fun getTopScores(limit: Int): List<ScoreWithUser> = scoreDao.topScores(limit)
}
