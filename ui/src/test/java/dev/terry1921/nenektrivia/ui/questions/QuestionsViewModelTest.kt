package dev.terry1921.nenektrivia.ui.questions

import dev.terry1921.nenektrivia.domain.preferences.GetUserSettingsUseCase
import dev.terry1921.nenektrivia.domain.questions.GetQuestionsUseCase
import dev.terry1921.nenektrivia.model.category.preference.UserSettings
import dev.terry1921.nenektrivia.model.question.QuestionModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
    private val getUserSettingsUseCase: GetUserSettingsUseCase = mock()

    @Test
    fun init_loadsFirstQuestionWithZeroPoints() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 3))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = false))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
        runCurrent()

        val state = viewModel.uiState.value
        assertNotNull(state.question)
        assertEquals(1, state.questionIndex)
        assertEquals(3, state.totalQuestions)
        assertEquals(0, state.points)
        assertFalse(state.isHapticsEnabled)
        assertFalse(state.revealAnswer)
    }

    @Test
    fun selectOption_correctAnswer_addsTenPointsAndNextQuestionDoesNotRepeat() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 3))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = true))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
        runCurrent()

        val firstState = viewModel.uiState.value
        val firstQuestionId = firstState.question!!.id
        val correctOptionId = firstState.question.options.first { it.isCorrect }.id

        viewModel.selectOption(correctOptionId)
        assertEquals(10, viewModel.uiState.value.points)
        assertTrue(viewModel.uiState.value.revealAnswer)
        assertFalse(viewModel.uiState.value.showGameOverDialog)

        viewModel.nextQuestion()
        runCurrent()

        val secondState = viewModel.uiState.value
        assertNotEquals(firstQuestionId, secondState.question!!.id)
        assertEquals(2, secondState.questionIndex)
    }

    @Test
    fun selectOption_wrongAnswer_keepsPointsAtZero() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = true))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
        runCurrent()

        val wrongOptionId = viewModel.uiState.value.question!!.options.first { !it.isCorrect }.id
        viewModel.selectOption(wrongOptionId)

        assertEquals(0, viewModel.uiState.value.points)
        assertTrue(viewModel.uiState.value.revealAnswer)
        assertTrue(viewModel.uiState.value.showGameOverDialog)
    }

    @Test
    fun selectOption_wrongAnswer_gameEndsUntilStartAgain() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 3))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = true))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
        runCurrent()

        val firstState = viewModel.uiState.value
        val firstQuestionId = firstState.question!!.id
        val wrongOptionId = firstState.question.options.first { !it.isCorrect }.id

        viewModel.selectOption(wrongOptionId)
        viewModel.nextQuestion()
        runCurrent()

        val gameOverState = viewModel.uiState.value
        assertTrue(gameOverState.showGameOverDialog)
        assertEquals(firstQuestionId, gameOverState.question!!.id)
        assertEquals(0, gameOverState.points)

        viewModel.startAgain()
        runCurrent()

        val restartedState = viewModel.uiState.value
        assertFalse(restartedState.showGameOverDialog)
        assertFalse(restartedState.revealAnswer)
        assertEquals(1, restartedState.questionIndex)
        assertEquals(0, restartedState.points)
    }

    @Test
    fun timerEndsWithoutAnswer_showsGameOverDialog() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = true))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
        runCurrent()

        advanceTimeBy(10_100)
        runCurrent()

        val state = viewModel.uiState.value
        assertEquals(0, state.points)
        assertTrue(state.revealAnswer)
        assertTrue(state.showGameOverDialog)
        assertEquals(0f, state.timeRemainingSeconds)
    }

    @Test
    fun completeGame_showsWinnerDialog() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = true))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
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
        assertFalse(state.showGameOverDialog)
        assertTrue(state.revealAnswer)
        assertEquals(0f, state.timeRemainingSeconds)
    }

    @Test
    fun onExitGame_resetsPointsAndWinnerState() = runTest {
        whenever(getQuestionsUseCase.invoke(false)).thenReturn(sampleQuestions(count = 2))
        whenever(getUserSettingsUseCase.invoke()).thenReturn(
            flowOf(UserSettings(isHapticsEnabled = true))
        )

        val viewModel = QuestionsViewModel(getQuestionsUseCase, getUserSettingsUseCase)
        runCurrent()

        val correctOptionId = viewModel.uiState.value.question!!.options.first { it.isCorrect }.id
        viewModel.selectOption(correctOptionId)
        assertEquals(10, viewModel.uiState.value.points)

        viewModel.onExitGame()

        val state = viewModel.uiState.value
        assertEquals(0, state.points)
        assertFalse(state.showWinnerDialog)
        assertFalse(state.showGameOverDialog)
        assertEquals(1, state.questionIndex)
        assertTrue(state.isHapticsEnabled)
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
