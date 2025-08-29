package dev.terry1921.nenektrivia.database.questions

import dev.terry1921.nenektrivia.database.dao.QuestionDao
import dev.terry1921.nenektrivia.database.entity.Question
import dev.terry1921.nenektrivia.database.relations.QuestionWithCategory

class RoomQuestionRepository(private val dao: QuestionDao) : QuestionRepository {
    override suspend fun getQuestionsByCategory(categoryId: String): List<QuestionWithCategory> =
        dao.getByCategory(categoryId)

    override suspend fun countByCategory(categoryId: String): Int = dao.countByCategory(categoryId)

    override suspend fun insertAll(questions: List<Question>) = dao.upsertAll(questions)
}
