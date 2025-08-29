package dev.terry1921.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.terry1921.database.entity.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(categories: List<Category>)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    suspend fun getAll(): List<Category>
}
