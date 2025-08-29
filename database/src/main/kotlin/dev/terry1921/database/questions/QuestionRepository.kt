package dev.terry1921.database.questions

import dev.terry1921.database.entity.Question
import dev.terry1921.database.relations.QuestionWithCategory

interface QuestionRepository {
    suspend fun getQuestionsByCategory(categoryId: String): List<QuestionWithCategory>

    suspend fun countByCategory(categoryId: String): Int

    suspend fun insertAll(questions: List<Question>)
}
