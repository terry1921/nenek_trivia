package dev.terry1921.nenektrivia.database.questions

import dev.terry1921.nenektrivia.database.entity.Question

interface QuestionRepository {
    suspend fun getAll(): List<Question>

    suspend fun countAll(): Int

    suspend fun replaceAll(questions: List<Question>)
}
