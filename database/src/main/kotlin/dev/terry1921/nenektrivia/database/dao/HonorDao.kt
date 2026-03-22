package dev.terry1921.nenektrivia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.terry1921.nenektrivia.database.entity.Honor

@Dao
interface HonorDao {
    @Query("SELECT * FROM honors WHERE id = :userId LIMIT 1")
    suspend fun findById(userId: String): Honor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(honor: Honor)
}
