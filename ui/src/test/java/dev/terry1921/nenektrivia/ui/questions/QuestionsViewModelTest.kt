package dev.terry1921.nenektrivia.ui.questions

import dev.terry1921.nenektrivia.domain.questions.GetQuestionsUseCase
import dev.terry1921.nenektrivia.model.question.QuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getQuestionsUseCase: GetQuestionsUseCase = mock()

    @Test
    fun init_loadsFirstQuestionWithZeroPoints() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 3))

        val viewModel = QuestionsViewModel(getQuestionsUseCase)
        runCurrent()

        val state = viewModel.uiState.value
        assertNotNull(state.question)
        assertEquals(1, state.questionIndex)
        assertEquals(3, state.totalQuestions)
        assertEquals(0, state.points)
        assertFalse(state.revealAnswer)
    }

    @Test
    fun selectOption_correctAnswer_addsTenPointsAndNextQuestionDoesNotRepeat() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 3))

        val viewModel = QuestionsViewModel(getQuestionsUseCase)
        runCurrent()

        val firstState = viewModel.uiState.value
        val firstQuestionId = firstState.question!!.id
        val correctOptionId = firstState.question.options.first { it.isCorrect }.id

        viewModel.selectOption(correctOptionId)
        assertEquals(10, viewModel.uiState.value.points)
        assertTrue(viewModel.uiState.value.revealAnswer)

        viewModel.nextQuestion()
        runCurrent()

        val secondState = viewModel.uiState.value
        assertNotEquals(firstQuestionId, secondState.question!!.id)
        assertEquals(2, secondState.questionIndex)
    }

    @Test
    fun selectOption_wrongAnswer_keepsPointsAtZero() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))

        val viewModel = QuestionsViewModel(getQuestionsUseCase)
        runCurrent()

        val wrongOptionId = viewModel.uiState.value.question!!.options.first { !it.isCorrect }.id
        viewModel.selectOption(wrongOptionId)

        assertEquals(0, viewModel.uiState.value.points)
        assertTrue(viewModel.uiState.value.revealAnswer)
    }

    @Test
    fun completeGame_showsWinnerDialog() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))

        val viewModel = QuestionsViewModel(getQuestionsUseCase)
        runCurrent()

        repeat(2) {
            val current = viewModel.uiState.value
            val correctOptionId = current.question!!.options.first { it.isCorrect }.id
            viewModel.selectOption(correctOptionId)
            viewModel.nextQuestion()
            runCurrent()
        }

        val state = viewModel.uiState.value
        assertTrue(state.showWinnerDialog)
        assertTrue(state.revealAnswer)
        assertEquals(0f, state.timeRemainingSeconds)
    }

    @Test
    fun onExitGame_resetsPointsAndWinnerState() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))

        val viewModel = QuestionsViewModel(getQuestionsUseCase)
        runCurrent()

        val correctOptionId = viewModel.uiState.value.question!!.options.first { it.isCorrect }.id
        viewModel.selectOption(correctOptionId)
        assertEquals(10, viewModel.uiState.value.points)

        viewModel.onExitGame()

        val state = viewModel.uiState.value
        assertEquals(0, state.points)
        assertFalse(state.showWinnerDialog)
        assertEquals(1, state.questionIndex)
    }

    private fun sampleQuestions(count: Int): List<QuestionModel> = (1..count).map { index ->
        QuestionModel(
            id = "q$index",
            question = "Pregunta $index",
            category = "Geografia",
            answerGood = "Correcta $index",
            answerBad01 = "Incorrecta 1 - $index",
            answerBad02 = "Incorrecta 2 - $index",
            answerBad03 = "Incorrecta 3 - $index",
            tip = "Tip $index"
        )
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(private val dispatcher: TestDispatcher = StandardTestDispatcher()) :
    TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
