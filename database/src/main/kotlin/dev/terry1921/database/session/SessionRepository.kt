package dev.terry1921.database.session

import dev.terry1921.database.entity.GameSession
import dev.terry1921.database.entity.SessionQuestionCrossRef
import dev.terry1921.database.relations.GameSessionWithUser

interface SessionRepository {
    suspend fun startSession(session: GameSession)

    suspend fun finishSession(
        sessionId: String,
        endedAt: Long,
        points: Int,
        answered: Int,
        correct: Int,
    )

    suspend fun recentSessionsForUser(
        userId: String,
        limit: Int,
    ): List<GameSessionWithUser>

    suspend fun upsertSessionQuestion(ref: SessionQuestionCrossRef)

    suspend fun correctCount(sessionId: String): Int
}
