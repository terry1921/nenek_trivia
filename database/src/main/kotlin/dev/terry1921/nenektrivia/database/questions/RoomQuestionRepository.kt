package dev.terry1921.nenektrivia.database.questions

import dev.terry1921.nenektrivia.database.dao.QuestionDao
import dev.terry1921.nenektrivia.database.entity.Question

class RoomQuestionRepository(private val dao: QuestionDao) : QuestionRepository {
    override suspend fun getAll(): List<Question> = dao.getAll()

    override suspend fun countAll(): Int = dao.countAll()

    override suspend fun replaceAll(questions: List<Question>) = dao.replaceAll(questions)
}
