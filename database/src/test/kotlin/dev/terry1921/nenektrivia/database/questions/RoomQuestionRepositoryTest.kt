package dev.terry1921.nenektrivia.database.questions

import com.google.common.truth.Truth.assertThat
import dev.terry1921.nenektrivia.database.dao.QuestionDao
import dev.terry1921.nenektrivia.database.entity.Question
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
    fun `getAll should call dao getAll`() = runTest {
        val expected = listOf(createQuestion())
        whenever(dao.getAll()).thenReturn(expected)

        val result = repository.getAll()

        verify(dao).getAll()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `countAll should call dao countAll`() = runTest {
        val expected = 5
        whenever(dao.countAll()).thenReturn(expected)

        val result = repository.countAll()

        verify(dao).countAll()
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `replaceAll should call dao replaceAll`() = runTest {
        val questions = listOf(createQuestion())

        repository.replaceAll(questions)

        verify(dao).replaceAll(questions)
    }

    private fun createQuestion() = Question(
        id = "1",
        question = "Q1",
        category = "Geografia",
        answerGood = "A",
        answerBad01 = "B",
        answerBad02 = "C",
        answerBad03 = "D",
        tip = "Tip"
    )
}
