package dev.terry1921.nenektrivia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.terry1921.nenektrivia.database.entity.Question
import dev.terry1921.nenektrivia.database.relations.QuestionWithCategory

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(questions: List<Question>)

    @Transaction
    @Query("SELECT * FROM questions WHERE category_id = :categoryId")
    suspend fun getByCategory(categoryId: String): List<QuestionWithCategory>

    @Query("SELECT COUNT(*) FROM questions WHERE category_id = :categoryId")
    suspend fun countByCategory(categoryId: String): Int
}
