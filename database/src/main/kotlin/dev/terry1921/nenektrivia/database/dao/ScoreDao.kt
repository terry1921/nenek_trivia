package dev.terry1921.nenektrivia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.terry1921.nenektrivia.database.entity.Score
import dev.terry1921.nenektrivia.database.relations.ScoreWithUser

@Dao
interface ScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(score: Score)

    @Transaction
    @Query("SELECT * FROM scores ORDER BY points DESC, created_at ASC LIMIT :limit")
    suspend fun topScores(limit: Int = 50): List<ScoreWithUser>
}
