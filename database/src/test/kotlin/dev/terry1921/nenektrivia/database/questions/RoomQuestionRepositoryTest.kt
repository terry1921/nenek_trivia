package dev.terry1921.nenektrivia.database.questions

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.QuestionDao
import dev.terry1921.nenektrivia.database.entity.Question
import dev.terry1921.nenektrivia.database.relations.QuestionWithCategory
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class RoomQuestionRepositoryTest {

    private lateinit var dao: QuestionDao
    private lateinit var repository: RoomQuestionRepository

    @Before
    fun setUp() {
        dao = mock()
        repository = RoomQuestionRepository(dao)
    }

    @Test
    fun `getQuestionsByCategory should call dao getByCategory`() = runTest {
        val categoryId = "1"
        val expected = emptyList<QuestionWithCategory>()
        whenever(dao.getByCategory(categoryId)).thenReturn(expected)

        val result = repository.getQuestionsByCategory(categoryId)

        verify(dao).getByCategory(categoryId)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `countByCategory should call dao countByCategory`() = runTest {
        val categoryId = "1"
        val expected = 5
        whenever(dao.countByCategory(categoryId)).thenReturn(expected)

        val result = repository.countByCategory(categoryId)

        verify(dao).countByCategory(categoryId)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `insertAll should call dao upsertAll`() = runTest {
        val questions = listOf(
            Question(
                id = "1",
                text = "Q1",
                options = listOf("A", "B", "C", "D"),
                correctIndex = 0,
                categoryId = 1
            )
        )

        repository.insertAll(questions)

        verify(dao).upsertAll(questions)
    }
}
