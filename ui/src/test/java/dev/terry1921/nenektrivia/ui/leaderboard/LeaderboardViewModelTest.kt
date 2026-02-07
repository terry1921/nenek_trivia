package dev.terry1921.nenektrivia.ui.leaderboard

import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun load_populatesLeaderboardAfterDelay() = runTest {
        val viewModel = LeaderboardViewModel()

        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
        assertTrue(loadingState.players.isEmpty())

        advanceTimeBy(400)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(expectedPlayers(), state.players)
    }

    @Test
    fun retry_resetsLoadingAndRepublishesPlayers() = runTest {
        val viewModel = LeaderboardViewModel()
        advanceTimeBy(400)
        advanceUntilIdle()

        viewModel.retry()
        runCurrent()

        val retryLoadingState = viewModel.uiState.value
        assertTrue(retryLoadingState.isLoading)
        assertTrue(retryLoadingState.players.isEmpty())

        advanceTimeBy(400)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(expectedPlayers(), state.players)
    }

    private fun expectedPlayers(): List<PlayerScore> = listOf(
        PlayerScore(1, null, "Terry1921", 1500),
        PlayerScore(2, null, "JaneDoe", 1200),
        PlayerScore(3, null, "JohnSmith", 1100),
        PlayerScore(4, null, "AliceWonder", 1000),
        PlayerScore(5, null, "BobBuilder", 900),
        PlayerScore(6, null, "CharlieBrown", 800),
        PlayerScore(7, null, "DoraExplorer", 700),
        PlayerScore(8, null, "EveOnline", 600),
        PlayerScore(9, null, "FrankCastle", 500),
        PlayerScore(10, null, "GraceHopper", 400)
    )
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
