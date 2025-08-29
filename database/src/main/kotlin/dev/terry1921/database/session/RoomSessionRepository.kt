package dev.terry1921.database.session

import dev.terry1921.database.dao.GameSessionDao
import dev.terry1921.database.dao.SessionQuestionDao
import dev.terry1921.database.entity.GameSession
import dev.terry1921.database.entity.SessionQuestionCrossRef
import dev.terry1921.database.relations.GameSessionWithUser

class RoomSessionRepository(
    private val sessionDao: GameSessionDao,
    private val sessionQuestionDao: SessionQuestionDao,
) : SessionRepository {
    override suspend fun startSession(session: GameSession) = sessionDao.start(session)

    override suspend fun finishSession(
        sessionId: String,
        endedAt: Long,
        points: Int,
        answered: Int,
        correct: Int,
    ) = sessionDao.finish(sessionId, endedAt, points, answered, correct)

    override suspend fun recentSessionsForUser(
        userId: String,
        limit: Int,
    ): List<GameSessionWithUser> = sessionDao.recentForUser(userId, limit)

    override suspend fun upsertSessionQuestion(ref: SessionQuestionCrossRef) = sessionQuestionDao.upsert(ref)

    override suspend fun correctCount(sessionId: String): Int = sessionQuestionDao.correctCount(sessionId)
}
