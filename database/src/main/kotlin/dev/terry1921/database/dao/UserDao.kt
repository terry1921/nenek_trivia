package dev.terry1921.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.terry1921.database.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    @Query("UPDATE users SET max_points = MAX(max_points, :points) WHERE id = :userId")
    suspend fun updateMaxPoints(
        userId: String,
        points: Int,
    )

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?
}
