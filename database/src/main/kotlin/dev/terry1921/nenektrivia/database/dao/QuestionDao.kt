package dev.terry1921.nenektrivia.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import dev.terry1921.nenektrivia.database.entity.Question

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(questions: List<Question>)

    @Query("SELECT * FROM questions")
    suspend fun getAll(): List<Question>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun countAll(): Int

    @Query("DELETE FROM questions")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(questions: List<Question>) {
        deleteAll()
        if (questions.isNotEmpty()) {
            upsertAll(questions)
        }
    }
}
