package dev.terry1921.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.terry1921.database.entity.SessionQuestionCrossRef

@Dao
interface SessionQuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(ref: SessionQuestionCrossRef)

    @Query("SELECT COUNT(*) FROM session_questions WHERE session_id = :sessionId AND is_correct = 1")
    suspend fun correctCount(sessionId: String): Int
}
