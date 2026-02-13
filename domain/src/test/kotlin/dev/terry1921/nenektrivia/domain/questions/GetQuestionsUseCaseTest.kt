package dev.terry1921.nenektrivia.domain.questions

import dev.terry1921.nenektrivia.database.entity.Question
import dev.terry1921.nenektrivia.database.questions.QuestionRepository
import dev.terry1921.nenektrivia.model.question.QuestionModel
import dev.terry1921.nenektrivia.model.question.RemoteQuestion
import dev.terry1921.nenektrivia.network.questions.QuestionsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetQuestionsUseCaseTest {

    private val localRepository: QuestionRepository = mock()
    private val remoteRepository: QuestionsRepository = mock()
    private val useCase = GetQuestionsUseCase(localRepository, remoteRepository)

    @Test
    fun invoke_withCachedLocalQuestions_returnsLocalWithoutRemoteFetch() = runTest {
        val local = listOf(
            Question(
                id = "q1",
                question = "Pregunta 1",
                category = "Geografia",
                answerGood = "A",
                answerBad01 = "B",
                answerBad02 = "C",
                answerBad03 = "D",
                tip = "Tip"
            )
        )
        whenever(localRepository.getAll()).thenReturn(local)

        val result = useCase(forceRefresh = false)

        assertEquals(local.map { it.toModel() }, result)
        verify(remoteRepository, never()).fetchQuestions()
        verify(localRepository, never()).replaceAll(any())
    }

    @Test
    fun invoke_withoutLocalQuestions_fetchesRemotePersistsAndReturnsMappedLocal() = runTest {
        val remote = listOf(
            RemoteQuestion(
                id = "q2",
                question = "Pregunta 2",
                category = "Geografia",
                answerGood = "Correcta",
                answerBad01 = "Mala 1",
                answerBad02 = "Mala 2",
                answerBad03 = "Mala 3",
                tip = "Tip remoto"
            )
        )
        val persisted = listOf(
            Question(
                id = "q2",
                question = "Pregunta 2",
                category = "Geografia",
                answerGood = "Correcta",
                answerBad01 = "Mala 1",
                answerBad02 = "Mala 2",
                answerBad03 = "Mala 3",
                tip = "Tip remoto"
            )
        )
        whenever(localRepository.getAll()).thenReturn(emptyList(), persisted)
        whenever(remoteRepository.fetchQuestions()).thenReturn(remote)

        val result = useCase(forceRefresh = false)

        verify(remoteRepository).fetchQuestions()
        verify(localRepository).replaceAll(eq(remote.map { it.toEntity() }))
        assertEquals(persisted.map { it.toModel() }, result)
    }

    @Test
    fun invoke_forceRefreshTrue_fetchesRemoteEvenWhenLocalExists() = runTest {
        val local = listOf(
            Question(
                id = "q3",
                question = "Pregunta local",
                category = "Geografia",
                answerGood = "A",
                answerBad01 = "B",
                answerBad02 = "C",
                answerBad03 = "D",
                tip = null
            )
        )
        val remote = listOf(
            RemoteQuestion(
                id = "q4",
                question = "Pregunta remota",
                category = "Geografia",
                answerGood = "Correcta",
                answerBad01 = "M1",
                answerBad02 = "M2",
                answerBad03 = "M3",
                tip = null
            )
        )
        val reloaded = listOf(
            Question(
                id = "q4",
                question = "Pregunta remota",
                category = "Geografia",
                answerGood = "Correcta",
                answerBad01 = "M1",
                answerBad02 = "M2",
                answerBad03 = "M3",
                tip = null
            )
        )
        whenever(localRepository.getAll()).thenReturn(reloaded)
        whenever(remoteRepository.fetchQuestions()).thenReturn(remote)

        val result = useCase(forceRefresh = true)

        verify(remoteRepository).fetchQuestions()
        verify(localRepository).replaceAll(eq(remote.map { it.toEntity() }))
        assertEquals(reloaded.map { it.toModel() }, result)
    }

    private fun Question.toModel() = QuestionModel(
        id = id,
        question = question,
        category = category,
        answerGood = answerGood,
        answerBad01 = answerBad01,
        answerBad02 = answerBad02,
        answerBad03 = answerBad03,
        tip = tip
    )
}
