package dev.terry1921.nenektrivia.ui.leaderboard

import dev.terry1921.nenektrivia.domain.leaderboard.GetLeaderboardUseCase
import dev.terry1921.nenektrivia.model.category.leaderboard.PlayerScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLeaderboardUseCase: GetLeaderboardUseCase = mock()

    @Test
    fun load_populatesLeaderboard() = runTest {
        whenever(getLeaderboardUseCase.invoke(false)).thenReturn(expectedPlayers())
        val viewModel = LeaderboardViewModel(getLeaderboardUseCase)

        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
        assertTrue(loadingState.players.isEmpty())

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(expectedPlayers(), state.players)
    }

    @Test
    fun retry_resetsLoadingAndRepublishesPlayers() = runTest {
        whenever(getLeaderboardUseCase.invoke(false)).thenReturn(expectedPlayers())
        whenever(getLeaderboardUseCase.invoke(true)).thenReturn(expectedPlayers())
        val viewModel = LeaderboardViewModel(getLeaderboardUseCase)
        advanceUntilIdle()

        viewModel.retry()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(expectedPlayers(), state.players)
        verify(getLeaderboardUseCase).invoke(true)
    }

    private fun expectedPlayers(): List<PlayerScore> = listOf(
        PlayerScore(id = "1", image = null, username = "Terry1921", points = 1500),
        PlayerScore(id = "2", image = null, username = "JaneDoe", points = 1200),
        PlayerScore(id = "3", image = null, username = "JohnSmith", points = 1100),
        PlayerScore(id = "4", image = null, username = "AliceWonder", points = 1000),
        PlayerScore(id = "5", image = null, username = "BobBuilder", points = 900),
        PlayerScore(id = "6", image = null, username = "CharlieBrown", points = 800),
        PlayerScore(id = "7", image = null, username = "DoraExplorer", points = 700),
        PlayerScore(id = "8", image = null, username = "EveOnline", points = 600),
        PlayerScore(id = "9", image = null, username = "FrankCastle", points = 500),
        PlayerScore(id = "10", image = null, username = "GraceHopper", points = 400)
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
