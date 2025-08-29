package dev.terry1921.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.terry1921.database.entity.GameSession
import dev.terry1921.database.relations.GameSessionWithUser

@Dao
interface GameSessionDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun start(session: GameSession)

    @Query(
        "UPDATE game_sessions SET ended_at = :endedAt, total_points = :points, questions_answered = :answered, correct_answers = :correct WHERE id = :sessionId",
    )
    suspend fun finish(
        sessionId: String,
        endedAt: Long,
        points: Int,
        answered: Int,
        correct: Int,
    )

    @Transaction
    @Query("SELECT * FROM game_sessions WHERE user_id = :userId ORDER BY started_at DESC LIMIT :limit")
    suspend fun recentForUser(
        userId: String,
        limit: Int = 10,
    ): List<GameSessionWithUser>
}
