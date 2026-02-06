package dev.terry1921.nenektrivia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.terry1921.nenektrivia.database.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: User)

    @Query("UPDATE users SET max_points = MAX(max_points, :points) WHERE id = :userId")
    suspend fun updateMaxPoints(userId: String, points: Int)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun findByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE is_logged_in = 1 LIMIT 1")
    suspend fun getActiveUser(): User?

    @Query("UPDATE users SET is_logged_in = 0")
    suspend fun clearActiveSession()

    @Query("UPDATE users SET is_logged_in = 1 WHERE id = :userId")
    suspend fun setActiveSession(userId: String)
}
